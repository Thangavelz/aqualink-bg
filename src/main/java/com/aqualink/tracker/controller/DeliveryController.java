package com.aqualink.tracker.controller;

import com.aqualink.tracker.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService service;

    @PostMapping("/deliver/{orderId}")
    public void deliver(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("orderId") Long orderId,
            @RequestParam("deliveredQty") int deliveredQty,
            @RequestParam("emptyCollected") int emptyCollected
    ) {
        service.markDelivered(vendorId, orderId, deliveredQty, emptyCollected);
    }

    @PostMapping("/skip/{orderId}")
    public void skip(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("orderId") Long orderId
    ) {
        service.markSkipped(vendorId, orderId);
    }
}