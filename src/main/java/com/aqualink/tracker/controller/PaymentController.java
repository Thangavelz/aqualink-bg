package com.aqualink.tracker.controller;

import com.aqualink.tracker.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public void add(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @RequestParam("customerId") Long customerId,
            @RequestParam("amount") double amount,
            @RequestParam("method") String method
    ) {
        service.addPayment(vendorId, customerId, amount, method);
    }
}