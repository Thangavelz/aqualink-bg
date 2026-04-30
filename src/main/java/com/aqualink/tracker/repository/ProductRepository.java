package com.aqualink.tracker.repository;

import com.aqualink.tracker.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByVendorIdAndIsActiveTrue(Long vendorId);
}