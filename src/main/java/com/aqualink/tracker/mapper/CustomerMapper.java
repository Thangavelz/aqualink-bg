package com.aqualink.tracker.mapper;

import com.aqualink.tracker.dto.response.CustomerResponse;
import com.aqualink.tracker.entity.Customer;

public class CustomerMapper {

    public static CustomerResponse toDto(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .phone(c.getPhone())
                .address(c.getAddress())
                .pricePerCan(c.getPricePerCan())
                .canBalance(c.getCanBalance())
                .dueAmount(c.getDueAmount())
                .build();
    }
}