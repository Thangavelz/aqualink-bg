package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.request.CustomerLoginRequest;
import com.aqualink.tracker.dto.request.CustomerSignupRequest;
import com.aqualink.tracker.dto.response.CustomerAuthResponse;
import com.aqualink.tracker.entity.Customer;
import com.aqualink.tracker.entity.CustomerNotification;
import com.aqualink.tracker.entity.Vendor;
import com.aqualink.tracker.repository.CustomerNotificationRepository;
import com.aqualink.tracker.repository.CustomerRepository;
import com.aqualink.tracker.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerRepository customerRepo;
    private final VendorRepository vendorRepo;
    private final CustomerNotificationRepository notifRepo;

    /**
     * Injected from SecurityConfig.passwordEncoder() @Bean.
     * Do NOT instantiate with 'new BCryptPasswordEncoder()' — that breaks @RequiredArgsConstructor
     * and creates an unmanaged instance outside Spring's lifecycle.
     */
    private final PasswordEncoder passwordEncoder;

    // ─── SIGNUP ──────────────────────────────────────────────────────────────

    @Transactional
    public CustomerAuthResponse signup(CustomerSignupRequest req) {

        // 1. Validate vendor code
        Vendor vendor = vendorRepo.findByVendorCode(req.getVendorCode().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid vendor code. Check the code on your water can."));

        // 2. Prevent duplicate phone per vendor
        customerRepo.findByPhoneAndVendorId(req.getPhone(), vendor.getId()).ifPresent(c -> {
            throw new RuntimeException("An account with this phone number already exists for this vendor.");
        });

        // 3. Create customer (status = PENDING until vendor approves)
        Customer c = new Customer();
        c.setVendorId(vendor.getId());
        c.setName(req.getName().trim());
        c.setPhone(req.getPhone().trim());
        c.setAddress(req.getAddress().trim());
        c.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        c.setVendorCode(req.getVendorCode().toUpperCase());
        c.setStatus("PENDING");

        customerRepo.save(c);

        return buildResponse(c, vendor, "");
    }

    // ─── LOGIN ───────────────────────────────────────────────────────────────

    public CustomerAuthResponse login(CustomerLoginRequest req) {

        Vendor vendor = vendorRepo.findByVendorCode(req.getVendorCode().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid vendor code."));

        Customer c = customerRepo.findByPhoneAndVendorId(req.getPhone(), vendor.getId())
                .orElseThrow(() -> new RuntimeException("No account found for this phone number."));

        if (!passwordEncoder.matches(req.getPassword(), c.getPasswordHash())) {
            throw new RuntimeException("Incorrect password.");
        }

        if ("REJECTED".equals(c.getStatus())) {
            throw new RuntimeException("Your account was not approved. Contact your vendor.");
        }

        return buildResponse(c, vendor, "");
    }

    // ─── VENDOR: APPROVE / REJECT ────────────────────────────────────────────

    @Transactional
    public void approveCustomer(Long vendorId, Long customerId) {

        Customer c = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        c.setStatus("ACTIVE");
        customerRepo.save(c);

        pushNotif(c, "ACCOUNT_APPROVED",
                "Account approved!",
                "You can now place orders with " + getVendorName(vendorId) + ".",
                null);
    }

    @Transactional
    public void rejectCustomer(Long vendorId, Long customerId, String reason) {

        Customer c = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        c.setStatus("REJECTED");
        customerRepo.save(c);

        pushNotif(c, "ACCOUNT_REJECTED",
                "Account not approved",
                reason != null ? reason : "Contact your vendor for details.",
                null);
    }

    // ─── VENDOR: LIST PENDING ─────────────────────────────────────────────────

    public List<Customer> getPendingCustomers(Long vendorId) {
        return customerRepo.findByVendorIdAndStatus(vendorId, "PENDING");
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    public void pushNotif(Customer customer, String type, String title, String body, Long refId) {
        CustomerNotification n = new CustomerNotification();
        n.setVendorId(customer.getVendorId());
        n.setCustomer(customer);
        n.setType(type);
        n.setTitle(title);
        n.setBody(body);
        n.setReferenceId(refId);
        notifRepo.save(n);
        // TODO: wire FCM here using customer's device token
    }

    private String getVendorName(Long vendorId) {
        return vendorRepo.findById(vendorId).map(Vendor::getName).orElse("your vendor");
    }

    private CustomerAuthResponse buildResponse(Customer c, Vendor vendor, String token) {
        return CustomerAuthResponse.builder()
                .customerId(c.getId())
                .name(c.getName())
                .phone(c.getPhone())
                .address(c.getAddress())
                .status(c.getStatus())
                .vendorId(vendor.getId())
                .vendorName(vendor.getName())
                .pricePerCan(c.getPricePerCan())
                .canBalance(c.getCanBalance())
                .dueAmount(c.getDueAmount())
                .token(token)
                .build();
    }
}