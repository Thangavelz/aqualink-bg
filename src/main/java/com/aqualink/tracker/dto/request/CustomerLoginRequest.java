package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class CustomerLoginRequest {
    private String phone;
    private String password;
    private String vendorCode;  // identifies which vendor's system to authenticate against
}
