package com.aqualink.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Getter @Setter
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String logoUrl;

    /**
     * Short unique code (e.g. "AQUA001") that customers enter at signup.
     * Printed on water cans / shared by the vendor verbally or on a card.
     */
    @Column(unique = true, nullable = false)
    private String vendorCode;

    /** BCrypt-hashed password for vendor login. */
    @JsonIgnore
    private String passwordHash;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
