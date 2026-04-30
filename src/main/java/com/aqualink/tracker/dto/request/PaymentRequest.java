package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long customerId;
    private Double amount;
    private String method; // CASH / UPI
}