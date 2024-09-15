package com.lorcan.bet.asyn.controller;

import com.lorcan.bet.asyn.dto.InventoryDto;
import com.lorcan.bet.asyn.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/inv")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Endpoint to deduct inventory
    @PutMapping("/deduct/{productId}")
    public ResponseEntity<InventoryDto> deductInventory(@PathVariable Long productId, @RequestParam int quantity) {
        InventoryDto updatedInventory = inventoryService.updateInventory(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    // Endpoint to add inventory
    @PostMapping("/add/{productId}")
    public ResponseEntity<InventoryDto> addInventory(@PathVariable Long productId, @RequestParam int quantity) {
        InventoryDto updatedInventory = inventoryService.addInventory(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }
}
