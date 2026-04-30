package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LedgerResponse {

    private String type; // DEBIT / CREDIT
    private Double amount;

    private String referenceType;
    private Long referenceId;

    private String createdAt;
}