package com.lorcan.bet.asyn.service;

import com.lorcan.bet.asyn.dto.OrderDto;
import com.lorcan.bet.asyn.entity.Inventory;
import com.lorcan.bet.asyn.entity.Order;
import com.lorcan.bet.asyn.entity.OrderLog;
import com.lorcan.bet.asyn.exception.InsufficientStockException;
import com.lorcan.bet.asyn.repository.InventoryRepository;
import com.lorcan.bet.asyn.repository.OrderLogRepository;
import com.lorcan.bet.asyn.repository.OrderRepository;
import com.lorcan.bet.asyn.util.OrderStatus;
import com.lorcan.bet.asyn.util.OrderUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderLogRepository orderLogRepository;

    @Async
    @Transactional
    public CompletableFuture<OrderDto> processOrder(Long orderId) {

        OrderDto dto = null;
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return CompletableFuture.completedFuture(new OrderDto(orderId, "Not Found"));  // Order not found
        }

        Order order = optionalOrder.get();
        if (order.getStatus() != OrderStatus.PENDING) {
            dto = OrderUtil.convertToDto(order);
            dto.setMessage("Order already Processed or Failed");
            return CompletableFuture.completedFuture(dto);
        }

        try {
            Inventory inventory = inventoryRepository.findByProductIdWithLock(order.getProduct().getId());
            if (inventory.getQuantity() >= order.getQuantity()) {
                // Deduct stock and mark order as processed
                inventory.setQuantity(inventory.getQuantity() - order.getQuantity());
                inventoryRepository.save(inventory);

                order.setStatus(OrderStatus.PROCESSED);
                order.setErrorMessage(null);
                orderRepository.save(order);

                logOrder(order, OrderStatus.PROCESSED, null);
                dto = OrderUtil.convertToDto(order);
                dto.setMessage("Order processed successfully");

            } else {
                // Insufficient stock, retry after a delay
                throw new InsufficientStockException("Not enough stock available for product " + order.getProduct().getName());
            }

        } catch (InsufficientStockException e) {
            // Log failure and retry
            logOrder(order, OrderStatus.FAILED, e.getMessage());
            retryOrder(orderId, e.getMessage());

        } catch (Exception e) {
            // Log general failure and don't retry
            logOrder(order, OrderStatus.FAILED, e.getMessage());
            dto = new OrderDto(orderId, e.getMessage());
            dto.setStatus(OrderStatus.FAILED.name());
        }

        return CompletableFuture.completedFuture(dto);
    }

    private void retryOrder(Long orderId, String errorMessage) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            System.out.println("Retrying order " + orderId);
            processOrder(orderId);  // Retry after delay
        }, 10, TimeUnit.SECONDS);  // Retry after 5 seconds (can use exponential backoff here)

        scheduler.shutdown();
    }

    private void logOrder(Order order, OrderStatus status, String errorMessage) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrder(order);
        orderLog.setStatus(status);
        orderLog.setProcessedAt(LocalDateTime.now());
        orderLog.setErrorMessage(errorMessage);
        orderLogRepository.save(orderLog);
    }

    public OrderDto createOrder(OrderDto orderDTO) {

        Order order = new Order();
        order.setProduct(orderDTO.getProduct());
        order.setQuantity(orderDTO.getQuantity());
        order.setStatus(OrderStatus.PENDING); // Set the initial status to PENDING

        // Save the order to the database
        Order savedOrder = orderRepository.save(order);
        logOrder(savedOrder, OrderStatus.PENDING, "Order Created");
        return OrderUtil.convertToDto(order);
    }
}
