package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByVendorIdAndIsActiveTrue(Long vendorId);

    List<Subscription> findByVendorIdAndNextRunDateLessThanEqual(Long vendorId, LocalDate date);

    List<Subscription> findByCustomerIdAndVendorId(Long customerId, Long vendorId);
}