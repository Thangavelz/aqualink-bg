package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOrderResponse {
    private Long   orderId;
    private Integer quantity;
    private Double totalAmount;
    private String scheduledDate;
    private String status;
    private String estimatedTime;
    private String note;
    private String createdAt;
    private Integer deliveredQty;
    private Integer emptyCollected;
}