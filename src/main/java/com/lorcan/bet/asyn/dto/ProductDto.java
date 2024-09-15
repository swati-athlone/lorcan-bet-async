package com.lorcan.bet.asyn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDto {

    private Long  id;
    private String name;
    private String description;
    private Double price;
    private String message;

    public ProductDto(){
    }

    public ProductDto(Long id, String message){
        this.id = id;
        this.message = message;
    }
}
