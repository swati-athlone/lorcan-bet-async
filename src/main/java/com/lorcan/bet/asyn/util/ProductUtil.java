package com.lorcan.bet.asyn.util;

import com.lorcan.bet.asyn.dto.ProductDto;
import com.lorcan.bet.asyn.entity.Product;

public class ProductUtil {

    public static ProductDto convertToDto(Product product){
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        return dto;
    }
}
