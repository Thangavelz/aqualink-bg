package com.aqualink.tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "order_requests")
@Getter @Setter
public class OrderRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Integer quantity;

    private LocalDate requestedDate;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;
}