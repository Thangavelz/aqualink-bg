package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.CustomerNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Long> {

    List<CustomerNotification> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    long countByCustomerIdAndIsReadFalse(Long customerId);
}
