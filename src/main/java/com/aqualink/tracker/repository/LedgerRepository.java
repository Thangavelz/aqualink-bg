package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findByVendorIdAndCustomerIdOrderByCreatedAtDesc(
            Long vendorId,
            Long customerId
    );

    List<LedgerEntry> findByVendorIdAndReferenceTypeAndReferenceId(
            Long vendorId,
            String referenceType,
            Long referenceId
    );
}