package com.aqualink.tracker.mapper;

import com.aqualink.tracker.dto.response.OrderResponse;
import com.aqualink.tracker.entity.Order;

public class OrderMapper {

    public static OrderResponse toDto(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .customerId(o.getCustomer().getId())
                .customerName(o.getCustomer().getName())
                .status(o.getStatus())
                .scheduledDate(o.getScheduledDate().toString())
                .totalAmount(o.getTotalAmount())
                .build();
    }
}