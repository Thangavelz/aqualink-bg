package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /** Used in CustomerOrderService to bulk-load items and avoid N+1 */
    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}