package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByVendorId(Long vendorId);

    Optional<Customer> findByIdAndVendorId(Long id, Long vendorId);

    List<Customer> findByVendorIdAndIsActiveTrue(Long vendorId);

    Optional<Customer> findByPhoneAndVendorId(String phone, Long vendorId);
}