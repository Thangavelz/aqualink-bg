package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderId(Long orderId);

    /** Used in CustomerOrderService to bulk-load deliveries and avoid N+1 */
    List<Delivery> findByOrderIdIn(List<Long> orderIds);
}