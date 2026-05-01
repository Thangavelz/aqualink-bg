package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.request.VendorLoginRequest;
import com.aqualink.tracker.dto.response.VendorLoginResponse;
import com.aqualink.tracker.service.VendorAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorAuthController {

    private final VendorAuthService authService;

    /**
     * POST /api/vendor/login
     * Body: { phone, password }
     * Returns vendor session info — store in localStorage as "vendor_session".
     */
    @PostMapping("/login")
    public ResponseEntity<VendorLoginResponse> login(
            @RequestBody VendorLoginRequest req
    ) {
        return ResponseEntity.ok(authService.login(req));
    }
}
