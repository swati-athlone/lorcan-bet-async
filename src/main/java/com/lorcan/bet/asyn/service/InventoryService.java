package com.lorcan.bet.asyn.service;

import com.lorcan.bet.asyn.dto.InventoryDto;
import com.lorcan.bet.asyn.entity.Inventory;
import com.lorcan.bet.asyn.entity.Product;
import com.lorcan.bet.asyn.repository.InventoryRepository;
import com.lorcan.bet.asyn.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    ProductRepository productRepository;

    // Method to check inventory availability
    public boolean checkInventory(Long productId, int requiredQuantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);

        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            return inventory.getQuantity() >= requiredQuantity;
        }

        return false;  // If inventory not found, consider as unavailable
    }

    public InventoryDto getInventory(Long productId) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);

        return inventoryOptional.map(this::mapToDTO).orElseGet(() -> (new InventoryDto(productId, "No inventory found for ProductID: " + productId)));

    }

    // Method to deduct inventory
    public InventoryDto updateInventory(Long productId, int quantityToDeduct) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductId(productId);

        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();

            // Check if there is enough quantity to deduct
            if (inventory.getQuantity() >= quantityToDeduct) {
                try {
                    inventory.setQuantity(inventory.getQuantity() - quantityToDeduct);

                    // Save the updated inventory, which will trigger optimistic locking
                    Inventory updatedInventory = inventoryRepository.save(inventory);

                    // Map to DTO and return
                    return mapToDTO(updatedInventory);
                } catch (OptimisticLockException e) {
                    throw new RuntimeException("Concurrent modification detected. Retry the operation.");
                }
            } else {
                throw new RuntimeException("Insufficient stock for product ID: " + productId);
            }
        } else {
            throw new RuntimeException("Inventory for product ID " + productId + " not found");
        }
    }

    // Method to add inventory (used when receiving stock or initializing inventory)
    public InventoryDto addInventory(Long productId, int quantity) {
        Product product = productRepository.getReferenceById(productId);
        Optional<Inventory> existingInventory = inventoryRepository.findByProductId(productId);

        Inventory inventory;
        if (existingInventory.isPresent()) {
            inventory = existingInventory.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else {
            inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        }

        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapToDTO(savedInventory, "Inventory Updated");
    }

    // Method to map Inventory entity to DTO
    private InventoryDto mapToDTO(Inventory inventory, String message) {
        InventoryDto dto = new InventoryDto();
        dto.setProductId(inventory.getProduct().getId());
        dto.setQuantity(inventory.getQuantity());
        dto.setMessage(message);
        return dto;
    }

    private InventoryDto mapToDTO(Inventory inventory) {
        InventoryDto dto = new InventoryDto();
        dto.setProductId(inventory.getProduct().getId());
        dto.setQuantity(inventory.getQuantity());
        return dto;
    }
}
