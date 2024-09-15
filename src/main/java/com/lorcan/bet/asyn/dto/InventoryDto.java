package com.lorcan.bet.asyn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InventoryDto {

    private Long id;
    private Long productId;
    private Integer quantity;
    private String message;
}
