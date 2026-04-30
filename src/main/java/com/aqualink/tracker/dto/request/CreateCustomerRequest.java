package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class CreateCustomerRequest {
    private String name;
    private String phone;
    private String address;
    private Double pricePerCan;
}