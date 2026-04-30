package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}