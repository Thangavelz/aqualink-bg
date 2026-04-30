package com.aqualink.tracker.mapper;

import com.aqualink.tracker.dto.response.TodayDeliveryResponse;
import com.aqualink.tracker.entity.Delivery;
import com.aqualink.tracker.entity.Order;
import com.aqualink.tracker.entity.OrderItem;

public class DeliveryMapper {

    public static TodayDeliveryResponse toDto(
            Order order,
            OrderItem item,
            Delivery delivery
    ) {

        return TodayDeliveryResponse.builder()
                .orderId(order.getId())

                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .address(order.getCustomer().getAddress())

                // ✅ REAL quantity
                .quantity(item != null ? item.getQuantity() : 0)

                // ✅ Delivery values (if already delivered)
                .deliveredQty(delivery != null ? delivery.getDeliveredQty() : 0)
                .emptyCollected(delivery != null ? delivery.getEmptyCollected() : 0)

                .canBalance(order.getCustomer().getCanBalance())
                .dueAmount(order.getCustomer().getDueAmount())

                .status(order.getStatus())
                .build();
    }
}