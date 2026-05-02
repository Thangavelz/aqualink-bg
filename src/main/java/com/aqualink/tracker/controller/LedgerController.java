package com.aqualink.tracker.controller;

import com.aqualink.tracker.entity.LedgerEntry;
import com.aqualink.tracker.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerRepository repo;

    @GetMapping("/{customerId}")
    public List<LedgerEntry> getLedger(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("customerId") Long customerId
    ) {
        return repo.findByVendorIdAndCustomerIdOrderByCreatedAtDesc(
                vendorId,
                customerId
        );
    }
}