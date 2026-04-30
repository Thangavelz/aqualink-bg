package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodayDeliveryResponse {

    private Long orderId;

    private Long customerId;
    private String customerName;
    private String address;

    private Integer quantity;

    private Integer deliveredQty;
    private Integer emptyCollected;

    private Integer canBalance;
    private Double dueAmount;

    private String status;

    public TodayDeliveryResponse(
            Long orderId,
            Long customerId,
            String customerName,
            String address,
            Integer quantity,
            Integer deliveredQty,
            Integer emptyCollected,
            Integer canBalance,
            Double dueAmount,
            String status
    ) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.quantity = quantity;
        this.deliveredQty = deliveredQty;
        this.emptyCollected = emptyCollected;
        this.canBalance = canBalance;
        this.dueAmount = dueAmount;
        this.status = status;
    }
}