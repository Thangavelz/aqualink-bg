package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class VendorLoginRequest {
    private String phone;
    private String password;
}
