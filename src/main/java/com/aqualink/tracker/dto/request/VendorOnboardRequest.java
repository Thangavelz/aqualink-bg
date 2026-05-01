package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class VendorOnboardRequest {
    private String companyName;
    private String phone;
    private String phone2;
    private String email;
    private String address;
    private String vendorCode;   // e.g. AQUA001 — auto-generated if blank
    private String password;
}
