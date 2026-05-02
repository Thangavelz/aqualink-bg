package com.aqualink.tracker.controller;

import com.aqualink.tracker.entity.Vendor;
import com.aqualink.tracker.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * DEV ONLY — Remove before production.
 * Exposes two utility endpoints:
 *   GET  /dev/hash?password=xxx          → returns the BCrypt hash
 *   POST /dev/vendor/{id}/set-password   → sets vendor password directly
 */
@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevToolsController {

    private final PasswordEncoder passwordEncoder;
    private final VendorRepository vendorRepo;

    /** GET /dev/hash?password=admin123  → shows the hash to paste into DB */
    @GetMapping("/hash")
    public String hash(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }

    /** POST /dev/vendor/2/set-password?password=admin123  → sets it directly */
    @PostMapping("/vendor/{id}/set-password")
    public String setPassword(@PathVariable("id") Long id, @RequestParam String password) {
        Vendor v = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        v.setPasswordHash(passwordEncoder.encode(password));
        vendorRepo.save(v);
        return "Password updated for vendor: " + v.getName();
    }
}