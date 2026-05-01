package com.aqualink.tracker.dto.request;

import lombok.Data;

@Data
public class CustomerSignupRequest {
    private String name;
    private String phone;
    private String address;
    private String password;

    /**
     * The short vendor code printed on the water can / given by the vendor.
     * e.g. "AQUA001"
     * Server validates this against Vendor.vendorCode and rejects if unknown.
     */
    private String vendorCode;
}
