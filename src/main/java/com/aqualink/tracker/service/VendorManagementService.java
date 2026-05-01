package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.request.VendorOnboardRequest;
import com.aqualink.tracker.dto.response.VendorOnboardResponse;
import com.aqualink.tracker.entity.Vendor;
import com.aqualink.tracker.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorManagementService {

    private final VendorRepository vendorRepo;
    private final PasswordEncoder  passwordEncoder;

    public List<VendorOnboardResponse> listAll() {
        return vendorRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public VendorOnboardResponse onboard(VendorOnboardRequest req) {
        // Generate vendorCode if not provided
        String code = (req.getVendorCode() != null && !req.getVendorCode().isBlank())
                ? req.getVendorCode().toUpperCase()
                : req.getCompanyName().replaceAll("[^A-Z0-9]", "").substring(0, Math.min(4,
                  req.getCompanyName().replaceAll("[^A-Za-z0-9]","").length())).toUpperCase()
                  + UUID.randomUUID().toString().substring(0,4).toUpperCase();

        if (vendorRepo.findByVendorCode(code).isPresent()) {
            throw new RuntimeException("Vendor code '" + code + "' already taken. Choose a different one.");
        }

        Vendor v = new Vendor();
        v.setName(req.getCompanyName().trim());
        v.setPhone(req.getPhone().trim());
        v.setPhone2(req.getPhone2() != null ? req.getPhone2().trim() : null);
        v.setEmail(req.getEmail() != null ? req.getEmail().trim().toLowerCase() : null);
        v.setAddress(req.getAddress().trim());
        v.setVendorCode(code);
        v.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        v.setStatus("ACTIVE");
        vendorRepo.save(v);

        return toDto(v);
    }

    private VendorOnboardResponse toDto(Vendor v) {
        return VendorOnboardResponse.builder()
                .vendorId(v.getId())
                .name(v.getName())
                .phone(v.getPhone())
                .phone2(v.getPhone2())
                .email(v.getEmail())
                .address(v.getAddress())
                .vendorCode(v.getVendorCode())
                .status(v.getStatus())
                .createdAt(v.getCreatedAt() != null ? v.getCreatedAt().toString() : "")
                .build();
    }
}
