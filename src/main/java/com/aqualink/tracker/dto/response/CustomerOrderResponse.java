package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOrderResponse {

    private Long   orderId;
    private int    quantity;
    private Double totalAmount;
    private String scheduledDate;

    /** PENDING | ACCEPTED | REJECTED | DELIVERED | SKIPPED */
    private String status;

    private String estimatedTime;   // nullable — for future ETA feature
    private String note;            // nullable — for future note-on-order feature

    private String createdAt;
    private int    deliveredQty;
    private int    emptyCollected;
}
