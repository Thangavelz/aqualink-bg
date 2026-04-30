package com.aqualink.tracker.controller;

import com.aqualink.tracker.entity.Subscription;
import com.aqualink.tracker.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping("/due")
    public List<Subscription> due(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return service.dueToday(vendorId);
    }
}