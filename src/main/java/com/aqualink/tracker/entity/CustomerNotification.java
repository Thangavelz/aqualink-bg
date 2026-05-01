package com.aqualink.tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer_notifications")
@Getter @Setter
public class CustomerNotification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** ORDER_PLACED | ORDER_ACCEPTED | ORDER_REJECTED | ORDER_DELIVERED | ACCOUNT_APPROVED | ACCOUNT_REJECTED */
    private String type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private Long referenceId;   // orderId if related to an order

    private Boolean isRead = false;
}
