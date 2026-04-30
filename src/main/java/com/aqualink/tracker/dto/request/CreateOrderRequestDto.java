package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class CreateOrderRequestDto {
    private Long customerId;
    private Integer quantity;
    private String requestedDate; // ISO: yyyy-MM-dd
}