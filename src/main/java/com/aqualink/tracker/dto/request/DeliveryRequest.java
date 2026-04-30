package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class DeliveryRequest {
    private Integer deliveredQty;
    private Integer emptyCollected;
}