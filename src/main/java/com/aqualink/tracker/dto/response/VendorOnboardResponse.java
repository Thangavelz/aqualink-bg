package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendorOnboardResponse {
    private Long   vendorId;
    private String name;
    private String phone;
    private String phone2;
    private String email;
    private String address;
    private String vendorCode;
    private String status;
    private String createdAt;
}
