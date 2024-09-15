package com.lorcan.bet.asyn.controller;

import com.lorcan.bet.asyn.dto.OrderDto;
import com.lorcan.bet.asyn.entity.Order;
import com.lorcan.bet.asyn.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/app/v1/order")
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping(value="/orders")
    public ResponseEntity<List<Order>> getOrdersById(@RequestBody Long orderId){
        return ResponseEntity.ok(new ArrayList<Order>());
    }

    @PostMapping(value = "/process")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDTO) throws ExecutionException, InterruptedException {
        // Step 1: Create the order in the system
        OrderDto createdOrder = orderService.createOrder(orderDTO);

        // Step 2: Asynchronously process the order
        CompletableFuture<OrderDto> dto = orderService.processOrder(createdOrder.getId());
        // Return the created order details
        return ResponseEntity.status(HttpStatus.OK).body(dto.get());
    }

}
