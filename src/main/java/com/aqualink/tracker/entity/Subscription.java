package com.aqualink.tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter @Setter
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Integer quantity;

    /**
     * Human-readable frequency label from the customer app.
     * DAILY | ALTERNATE | MWF | WEEKLY
     * Stored so the UI can display it back without re-deriving from frequencyDays.
     */
    private String frequency;

    /**
     * Delivery interval in days — used by the scheduler.
     * Derived from `frequency` at creation time.
     */
    private Integer frequencyDays;

    /**
     * The date the customer chose to start the subscription.
     * Also used as the first nextRunDate.
     */
    private LocalDate startDate;

    /** Next scheduled delivery date — updated by the scheduler after each run. */
    private LocalDate nextRunDate;

    private Boolean isActive = true;
}
