package com.aqualink.tracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerSubscriptionResponse {

    private Long    id;
    private int     quantity;
    private String  frequency;    // DAILY | ALTERNATE | MWF | WEEKLY
    private String  startDate;
    private String  nextRunDate;
    private boolean isActive;
}