package com.aqualink.tracker.controller;

import com.aqualink.tracker.entity.Order;
import com.aqualink.tracker.repository.OrderRepository;
import com.aqualink.tracker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final OrderRepository repo;

    @PostMapping("/accept/{requestId}")
    public Order accept(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("requestId") Long requestId
    ) {
        return service.acceptRequest(vendorId, requestId);
    }

    @GetMapping("/today")
    public List<Order> today(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return repo.findByVendorIdAndScheduledDate(vendorId, LocalDate.now());
    }
}