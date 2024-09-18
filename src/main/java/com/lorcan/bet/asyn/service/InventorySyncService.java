package com.lorcan.bet.asyn.service;

import com.lorcan.bet.asyn.dto.WarehouseInventoryDto;
import com.lorcan.bet.asyn.entity.Inventory;
import com.lorcan.bet.asyn.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InventorySyncService {

    private final WarehouseService warehouseService;
    private final InventoryRepository inventoryRepository;

    public InventorySyncService(WarehouseService warehouseService, InventoryRepository inventoryRepository) {
        this.warehouseService = warehouseService;
        this.inventoryRepository = inventoryRepository;
    }

    @Scheduled(fixedRateString = "${inventory.sync.rate:60000}") // Default to run every 60 seconds
    @Transactional
    public void syncInventoryWithWarehouse() {
        log.info("Starting inventory sync with external warehouse");

        List<WarehouseInventoryDto> warehouseInventory = warehouseService.fetchWarehouseInventory();
        if (warehouseInventory == null || warehouseInventory.isEmpty()) {
            log.warn("No inventory data received from the warehouse");
            return;
        }

        for (WarehouseInventoryDto warehouseItem : warehouseInventory) {
            Optional<Inventory> optionalInventory = inventoryRepository.findByProductId(warehouseItem.getProductId());

            if (optionalInventory.isPresent()) {
                Inventory inventory = optionalInventory.get();
                int currentStock = inventory.getQuantity();
                int warehouseStock = warehouseItem.getAvailableQuantity();

                // Update stock if there is a mismatch
                if (currentStock != warehouseStock) {
                    log.info("Updating stock for product ID {}: Local = {}, Warehouse = {}",
                            warehouseItem.getProductId(), currentStock, warehouseStock);
                    inventory.setQuantity(warehouseStock);
                    inventoryRepository.save(inventory);
                }
            } else {
                log.warn("Product with ID {} not found in local inventory. Skipping.", warehouseItem.getProductId());
            }
        }

        log.info("Inventory sync with warehouse completed successfully");
    }
}

