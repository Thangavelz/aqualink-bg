package com.aqualink.tracker.dto.response;

import com.aqualink.tracker.entity.OrderRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderRequestResponse {

    public Long id;
    public int quantity;
    public LocalDate requestedDate;
    public String status;
    public String notes;
    public LocalDateTime createdAt;
    public CustomerInfo customer;

    public static class CustomerInfo {
        public Long id;
        public String name;
        public String phone;
        public String address;
        public Double pricePerCan;
    }

    public static OrderRequestResponse from(OrderRequest r) {
        OrderRequestResponse dto = new OrderRequestResponse();
        dto.id           = r.getId();
        dto.quantity     = r.getQuantity() != null ? r.getQuantity() : 0;
        dto.requestedDate = r.getRequestedDate();
        dto.status       = r.getStatus();
        dto.notes        = r.getNotes();
        dto.createdAt    = r.getCreatedAt();

        if (r.getCustomer() != null) {
            CustomerInfo ci = new CustomerInfo();
            ci.id           = r.getCustomer().getId();
            ci.name         = r.getCustomer().getName();
            ci.phone        = r.getCustomer().getPhone();
            ci.address      = r.getCustomer().getAddress();
            ci.pricePerCan  = r.getCustomer().getPricePerCan();
            dto.customer    = ci;
        }
        return dto;
    }
}
