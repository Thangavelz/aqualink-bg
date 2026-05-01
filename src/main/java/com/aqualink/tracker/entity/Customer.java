package com.aqualink.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter @Setter
public class Customer extends BaseEntity {

    private String name;
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    /** Never expose the hash in any JSON response */
    @JsonIgnore
    private String passwordHash;

    /**
     * The vendor-issued invite code the customer enters at signup.
     * Matches Vendor.vendorCode — links the customer to the correct vendor.
     */
    @JsonIgnore  // internal — never expose to clients
    private String vendorCode;

    private Double pricePerCan = 0.0;

    private Integer canBalance = 0;
    private Double dueAmount = 0.0;

    /**
     * PENDING  → waiting for vendor approval
     * ACTIVE   → approved, can place orders
     * REJECTED → vendor rejected the signup
     */
    private String status = "PENDING";

    private Boolean isActive = true;
}