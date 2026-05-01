package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAuthResponse {
    private Long customerId;
    private String name;
    private String phone;
    private String address;
    private String status;          // PENDING | ACTIVE | REJECTED
    private Long vendorId;
    private String vendorName;
    private Double pricePerCan;
    private Integer canBalance;
    private Double dueAmount;
    private String token;           // JWT — empty string until JWT is wired in
}
