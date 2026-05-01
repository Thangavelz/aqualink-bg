package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.response.OrderResponse;
import com.aqualink.tracker.mapper.OrderMapper;
import com.aqualink.tracker.repository.OrderRepository;
import com.aqualink.tracker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final OrderRepository repo;

    /**
     * POST /api/orders/accept/{requestId}
     * Vendor accepts a pending OrderRequest → creates Order + OrderItem.
     * Returns a safe DTO (no lazy-loaded entities exposed).
     */
    @PostMapping("/accept/{requestId}")
    public OrderResponse accept(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable Long requestId
    ) {
        return OrderMapper.toDto(service.acceptRequest(vendorId, requestId));
    }

    /**
     * GET /api/orders/today
     * Returns all orders scheduled for today as safe DTOs.
     * Uses EAGER join via JPQL so Customer is always loaded.
     */
    @GetMapping("/today")
    public List<OrderResponse> today(@RequestHeader("X-VENDOR-ID") Long vendorId) {
        return repo.findTodayOrders(vendorId, LocalDate.now())
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
}