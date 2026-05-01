package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendorLoginResponse {
    private Long   vendorId;
    private String name;
    private String phone;
    private String vendorCode;
    private String logoUrl;
}
