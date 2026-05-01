package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.request.AdminLoginRequest;
import com.aqualink.tracker.dto.response.AdminLoginResponse;
import com.aqualink.tracker.entity.Admin;
import com.aqualink.tracker.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminLoginResponse login(AdminLoginRequest req) {
        Admin admin = adminRepo.findByEmail(req.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("No admin account found for this email."));

        if (!passwordEncoder.matches(req.getPassword(), admin.getPasswordHash())) {
            throw new RuntimeException("Incorrect password.");
        }

        return AdminLoginResponse.builder()
                .adminId(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .build();
    }
}
