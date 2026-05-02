package com.aqualink.tracker.repository;

import com.aqualink.tracker.dto.response.TodayDeliveryResponse;
import com.aqualink.tracker.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndVendorId(Long id, Long vendorId);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.id = :id")
    Optional<Order> findByIdWithCustomer(@Param("id") Long id);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.customer.id = :customerId AND o.vendorId = :vendorId ORDER BY o.scheduledDate DESC")
    List<Order> findByCustomerIdAndVendorIdOrderByScheduledDateDesc(
            @Param("customerId") Long customerId,
            @Param("vendorId") Long vendorId);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer c WHERE o.vendorId = :vendorId AND o.scheduledDate = :date ORDER BY c.name ASC")
    List<Order> findTodayOrders(@Param("vendorId") Long vendorId, @Param("date") LocalDate date);

    @Query("""
        SELECT new com.aqualink.tracker.dto.response.TodayDeliveryResponse(
            o.id, c.id, c.name, c.address,
            COALESCE(oi.quantity, 0),
            COALESCE(d.deliveredQty, 0),
            COALESCE(d.emptyCollected, 0),
            c.canBalance, c.dueAmount, o.status
        )
        FROM Order o
        JOIN o.customer c
        LEFT JOIN OrderItem oi ON oi.order.id = o.id
        LEFT JOIN Delivery d ON d.order.id = o.id
        WHERE o.vendorId = :vendorId
          AND o.scheduledDate = :date
          AND o.status IN ('PENDING','ACCEPTED','DELIVERED','PARTIAL','SKIPPED')
        ORDER BY c.name ASC
    """)
    List<TodayDeliveryResponse> findTodayDelivery(
            @Param("vendorId") Long vendorId,
            @Param("date") LocalDate date);

    // ── History queries — separate methods per filter combo (avoids PostgreSQL null param issue) ──

    /** All statuses, all dates */
    @Query("""
        SELECT new com.aqualink.tracker.dto.response.TodayDeliveryResponse(
            o.id, c.id, c.name, c.address,
            COALESCE(oi.quantity, 0), COALESCE(d.deliveredQty, 0), COALESCE(d.emptyCollected, 0),
            c.canBalance, c.dueAmount, o.status
        )
        FROM Order o JOIN o.customer c
        LEFT JOIN OrderItem oi ON oi.order.id = o.id
        LEFT JOIN Delivery d ON d.order.id = o.id
        WHERE o.vendorId = :vendorId
          AND o.scheduledDate >= :from AND o.scheduledDate <= :to
        ORDER BY o.scheduledDate DESC, c.name ASC
    """)
    List<TodayDeliveryResponse> findHistoryByDateRange(
            @Param("vendorId") Long vendorId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    /** Specific status + date range */
    @Query("""
        SELECT new com.aqualink.tracker.dto.response.TodayDeliveryResponse(
            o.id, c.id, c.name, c.address,
            COALESCE(oi.quantity, 0), COALESCE(d.deliveredQty, 0), COALESCE(d.emptyCollected, 0),
            c.canBalance, c.dueAmount, o.status
        )
        FROM Order o JOIN o.customer c
        LEFT JOIN OrderItem oi ON oi.order.id = o.id
        LEFT JOIN Delivery d ON d.order.id = o.id
        WHERE o.vendorId = :vendorId
          AND o.status = :status
          AND o.scheduledDate >= :from AND o.scheduledDate <= :to
        ORDER BY o.scheduledDate DESC, c.name ASC
    """)
    List<TodayDeliveryResponse> findHistoryByStatusAndDateRange(
            @Param("vendorId") Long vendorId,
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}