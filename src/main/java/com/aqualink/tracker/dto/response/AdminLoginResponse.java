package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginResponse {
    private Long   adminId;
    private String name;
    private String email;
}
