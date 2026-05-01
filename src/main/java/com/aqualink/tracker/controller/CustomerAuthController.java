package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.request.CustomerLoginRequest;
import com.aqualink.tracker.dto.request.CustomerSignupRequest;
import com.aqualink.tracker.dto.response.CustomerAuthResponse;
import com.aqualink.tracker.entity.Customer;
import com.aqualink.tracker.service.CustomerAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final CustomerAuthService authService;

    // ─── PUBLIC: SIGNUP / LOGIN ───────────────────────────────────────────────

    /**
     * POST /api/customer/signup
     * Body: { vendorCode, name, phone, address, password }
     */
    @PostMapping("/signup")
    public ResponseEntity<CustomerAuthResponse> signup(
            @RequestBody CustomerSignupRequest req
    ) {
        return ResponseEntity.ok(authService.signup(req));
    }

    /**
     * POST /api/customer/login
     * Body: { vendorCode, phone, password }
     */
    @PostMapping("/login")
    public ResponseEntity<CustomerAuthResponse> login(
            @RequestBody CustomerLoginRequest req
    ) {
        return ResponseEntity.ok(authService.login(req));
    }

    // ─── VENDOR: MANAGE CUSTOMER APPROVALS ───────────────────────────────────

    /**
     * GET /api/customer/pending
     * Header: X-VENDOR-ID
     * Returns all customers waiting for approval.
     *
     * Postman: GET http://localhost:8080/api/customer/pending
     *          X-VENDOR-ID: 1
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Customer>> getPending(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return ResponseEntity.ok(authService.getPendingCustomers(vendorId));
    }

    /**
     * POST /api/customer/approve/{customerId}
     * Header: X-VENDOR-ID
     * Approves the customer → status = ACTIVE, sends notification.
     *
     * Postman: POST http://localhost:8080/api/customer/approve/1
     *          X-VENDOR-ID: 1
     */
    @PostMapping("/approve/{customerId}")
    public ResponseEntity<Void> approve(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("customerId") Long customerId
    ) {
        authService.approveCustomer(vendorId, customerId);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/customer/reject/{customerId}
     * Header: X-VENDOR-ID
     * Body (optional): { "reason": "Duplicate account" }
     * Rejects the customer → status = REJECTED, sends notification.
     *
     * Postman: POST http://localhost:8080/api/customer/reject/1
     *          X-VENDOR-ID: 1
     *          Body: { "reason": "Outside delivery area" }
     */
    @PostMapping("/reject/{customerId}")
    public ResponseEntity<Void> reject(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("customerId") Long customerId
,            @RequestBody(required = false) Map<String, String> body
    ) {
        String reason = body != null ? body.get("reason") : null;
        authService.rejectCustomer(vendorId, customerId, reason);
        return ResponseEntity.ok().build();
    }
}