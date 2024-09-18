package com.lorcan.bet.asyn.dto;

import lombok.Data;

@Data
public class WarehouseInventoryDto {
    private Long productId;
    private int availableQuantity;
}
