package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

    List<OrderRequest> findByVendorIdAndStatus(Long vendorId, String status);

    List<OrderRequest> findByVendorIdAndRequestedDate(Long vendorId, LocalDate date);

    List<OrderRequest> findByVendorIdAndCustomerId(Long vendorId, Long customerId);
}