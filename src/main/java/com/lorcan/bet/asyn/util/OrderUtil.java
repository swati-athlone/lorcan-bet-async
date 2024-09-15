package com.lorcan.bet.asyn.util;

import com.lorcan.bet.asyn.dto.OrderDto;
import com.lorcan.bet.asyn.entity.Order;

public class OrderUtil {

    public static OrderDto convertToDto(Order order){

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setProduct(order.getProduct());
        dto.setQuantity(order.getQuantity());
        dto.setStatus(order.getStatus().name());
        return dto;
    }
}
