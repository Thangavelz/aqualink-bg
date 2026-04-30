package com.aqualink.tracker.entity;

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

    private Double pricePerCan;

    private Integer canBalance = 0;
    private Double dueAmount = 0.0;

    private Boolean isActive = true;
}