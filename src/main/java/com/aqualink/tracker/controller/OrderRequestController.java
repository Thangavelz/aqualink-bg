package com.aqualink.tracker.controller;

import com.aqualink.tracker.entity.OrderRequest;
import com.aqualink.tracker.service.OrderRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class OrderRequestController {

    private final OrderRequestService service;

    @PostMapping
    public OrderRequest create(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @RequestParam("customerId") Long customerId,
            @RequestParam("quantity") int quantity,
            @RequestParam("date") String date
    ) {
        return service.create(
                vendorId,
                customerId,
                quantity,
                LocalDate.parse(date)
        );
    }

    @GetMapping("/pending")
    public List<OrderRequest> pending(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return service.pending(vendorId);
    }
}