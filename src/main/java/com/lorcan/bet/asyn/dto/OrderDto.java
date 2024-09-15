package com.lorcan.bet.asyn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lorcan.bet.asyn.entity.Product;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDto {

    private Long  id;
    private Integer quantity;
    private String status;
    private String message;
    private Product product;

    public OrderDto(){

    }

    public OrderDto(Long id, String message){
        this.id = id;
        this.message = message;
    }
}
