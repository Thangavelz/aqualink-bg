package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.request.AdminLoginRequest;
import com.aqualink.tracker.dto.response.AdminLoginResponse;
import com.aqualink.tracker.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService authService;

    /** POST /api/admin/login  — body: { email, password } */
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
