package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private Long customerId;
    private String customerName;

    private String status;
    private String scheduledDate;

    private Double totalAmount;
}