package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.request.VendorLoginRequest;
import com.aqualink.tracker.dto.response.VendorLoginResponse;
import com.aqualink.tracker.entity.Vendor;
import com.aqualink.tracker.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorAuthService {

    private final VendorRepository vendorRepo;
    private final PasswordEncoder  passwordEncoder;

    public VendorLoginResponse login(VendorLoginRequest req) {

        Vendor vendor = vendorRepo.findByPhone(req.getPhone().trim())
                .orElseThrow(() -> new RuntimeException("No vendor account found for this phone number."));

        if (vendor.getPasswordHash() == null) {
            throw new RuntimeException("Password not set for this account. Contact support.");
        }

        if (!passwordEncoder.matches(req.getPassword(), vendor.getPasswordHash())) {
            throw new RuntimeException("Incorrect password.");
        }

        return VendorLoginResponse.builder()
                .vendorId(vendor.getId())
                .name(vendor.getName())
                .phone(vendor.getPhone())
                .vendorCode(vendor.getVendorCode())
                .logoUrl(vendor.getLogoUrl())
                .build();
    }
}
