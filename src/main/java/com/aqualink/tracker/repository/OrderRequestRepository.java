package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

    // Eagerly fetch customer to avoid LazyInitializationException during DTO mapping
    @Query("SELECT r FROM OrderRequest r JOIN FETCH r.customer WHERE r.vendorId = :vendorId AND r.status = :status ORDER BY r.createdAt DESC")
    List<OrderRequest> findByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") String status);

    List<OrderRequest> findByVendorIdAndRequestedDate(Long vendorId, LocalDate date);

    List<OrderRequest> findByVendorIdAndCustomerId(Long vendorId, Long customerId);

    @Query("SELECT r FROM OrderRequest r JOIN FETCH r.customer WHERE r.id = :id")
    Optional<OrderRequest> findByIdWithCustomer(@Param("id") Long id);
}