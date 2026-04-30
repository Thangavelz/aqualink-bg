package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByVendorIdAndCustomerId(Long vendorId, Long customerId);
}