package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.response.CustomerSubscriptionResponse;
import com.aqualink.tracker.service.CustomerSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/subscriptions")
@RequiredArgsConstructor
public class CustomerSubscriptionController {

    private final CustomerSubscriptionService subService;

    /**
     * GET /api/customer/subscriptions
     * Returns all subscriptions for the logged-in customer.
     */
    @GetMapping
    public ResponseEntity<List<CustomerSubscriptionResponse>> getAll(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId
    ) {
        return ResponseEntity.ok(subService.getAll(customerId, vendorId));
    }

    /**
     * POST /api/customer/subscriptions
     * Body: { quantity, frequency, startDate }
     */
    @PostMapping
    public ResponseEntity<CustomerSubscriptionResponse> create(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId,
            @RequestBody Map<String, Object> body
    ) {
        int    quantity  = Integer.parseInt(body.get("quantity").toString());
        String frequency = body.get("frequency").toString();
        String startDate = body.get("startDate").toString();
        return ResponseEntity.ok(subService.create(customerId, vendorId, quantity, frequency, startDate));
    }

    /**
     * DELETE /api/customer/subscriptions/{id}
     * Soft-cancels the subscription (isActive = false).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId,
            @PathVariable Long id
    ) {
        subService.cancel(customerId, vendorId, id);
        return ResponseEntity.noContent().build();
    }
}