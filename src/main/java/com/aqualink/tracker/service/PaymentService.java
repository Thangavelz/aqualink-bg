package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.*;
import com.aqualink.tracker.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final LedgerRepository ledgerRepo;
    private final CustomerRepository customerRepo;

    @Transactional
    public void addPayment(Long vendorId, Long customerId, double amount, String method) {

        Customer customer = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Payment p = new Payment();
        p.setVendorId(vendorId);
        p.setCustomer(customer);
        p.setAmount(amount);
        p.setMethod(method);

        paymentRepo.save(p);

        LedgerEntry l = new LedgerEntry();
        l.setVendorId(vendorId);
        l.setCustomer(customer);
        l.setType("CREDIT");
        l.setAmount(amount);
        l.setReferenceType("PAYMENT");
        l.setReferenceId(p.getId());

        ledgerRepo.save(l);

        customer.setDueAmount(customer.getDueAmount() - amount);
        customerRepo.save(customer);
    }
}