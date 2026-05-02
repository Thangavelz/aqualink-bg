package com.aqualink.tracker.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aqualink.tracker.dto.response.OrderRequestResponse;
import com.aqualink.tracker.service.OrderRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class OrderRequestController {

    private final OrderRequestService service;

    @PostMapping
    public OrderRequestResponse create(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @RequestParam("customerId") Long customerId,
            @RequestParam("quantity") int quantity,
            @RequestParam("date") String date
    ) {
        return OrderRequestResponse.from(
                service.create(vendorId, customerId, quantity, LocalDate.parse(date))
        );
    }

    @GetMapping("/pending")
    public List<OrderRequestResponse> pending(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return service.pending(vendorId)
                .stream()
                .map(OrderRequestResponse::from)
                .collect(Collectors.toList());
    }
}