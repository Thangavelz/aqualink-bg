package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.response.CustomerOrderResponse;
import com.aqualink.tracker.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService orderService;

    /**
     * GET /api/customer/orders
     * Headers: X-CUSTOMER-ID, X-VENDOR-ID  (injected by axios interceptor)
     * Returns full order history for the logged-in customer, newest first.
     */
    @GetMapping
    public ResponseEntity<List<CustomerOrderResponse>> getHistory(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId
    ) {
        return ResponseEntity.ok(orderService.getHistory(customerId, vendorId));
    }

    /**
     * POST /api/customer/orders?quantity=2&date=2026-05-02&note=...
     * Headers: X-CUSTOMER-ID, X-VENDOR-ID  (injected by axios interceptor)
     * Places a new order request and immediately accepts it (auto-confirm flow).
     */
    @PostMapping
    public ResponseEntity<CustomerOrderResponse> placeOrder(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId,
            @RequestParam("quantity")        int quantity,
            @RequestParam("date")            String date,
            @RequestParam(value = "note", defaultValue = "") String note
    ) {
        return ResponseEntity.ok(orderService.placeOrder(customerId, vendorId, quantity, date, note));
    }
}
