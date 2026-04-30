package com.aqualink.tracker.controller;

import com.aqualink.tracker.entity.Customer;
import com.aqualink.tracker.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public Customer create(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @RequestBody Customer c
    ) {
        return service.create(vendorId, c);
    }

    @GetMapping
    public List<Customer> getAll(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return service.getAll(vendorId);
    }

    @GetMapping("/{id}")
    public Customer get(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable Long id
    ) {
        return service.get(vendorId, id);
    }

    @PutMapping("/{id}")
    public Customer update(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable Long id,
            @RequestBody Customer c
    ) {
        return service.update(vendorId, id, c);
    }
}