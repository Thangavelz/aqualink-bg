package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.response.CustomerOrderResponse;
import com.aqualink.tracker.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService orderService;

    /**
     * GET /api/customer/orders
     * Returns full order history for the logged-in customer,
     * including deliveredQty and emptyCollected from the Delivery table.
     */
    @GetMapping
    public List<CustomerOrderResponse> getHistory(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId
    ) {
        return orderService.getHistory(customerId, vendorId);
    }

    /**
     * POST /api/customer/orders?quantity=2&date=2026-05-02&note=...
     */
    @PostMapping
    public CustomerOrderResponse placeOrder(
            @RequestHeader("X-CUSTOMER-ID") Long customerId,
            @RequestHeader("X-VENDOR-ID")   Long vendorId,
            @RequestParam("quantity")        int    quantity,
            @RequestParam("date")            String date,
            @RequestParam(value="note", required=false) String note
    ) {
        return orderService.placeOrder(customerId, vendorId, quantity, date, note);
    }
}