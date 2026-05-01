package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.request.CustomerPlaceOrderRequest;
import com.aqualink.tracker.dto.response.CustomerOrderResponse;
import com.aqualink.tracker.entity.*;
import com.aqualink.tracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final OrderRepository        orderRepo;
    private final OrderItemRepository    itemRepo;
    private final DeliveryRepository     deliveryRepo;
    private final CustomerRepository     customerRepo;

    // ── GET HISTORY ──────────────────────────────────────────────────────────

    public List<CustomerOrderResponse> getHistory(Long customerId, Long vendorId) {

        List<Order> orders = orderRepo
                .findByCustomerIdAndVendorIdOrderByScheduledDateDesc(customerId, vendorId);

        // Bulk-load OrderItems and Deliveries to avoid N+1
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());

        Map<Long, OrderItem> itemMap = itemRepo.findByOrderIdIn(orderIds)
                .stream().collect(Collectors.toMap(i -> i.getOrder().getId(), i -> i, (a, b) -> a));

        Map<Long, Delivery> deliveryMap = deliveryRepo.findByOrderIdIn(orderIds)
                .stream().collect(Collectors.toMap(d -> d.getOrder().getId(), d -> d, (a, b) -> a));

        return orders.stream().map(o -> {
            OrderItem item     = itemMap.get(o.getId());
            Delivery  delivery = deliveryMap.get(o.getId());
            return CustomerOrderResponse.builder()
                    .orderId(o.getId())
                    .quantity(item != null ? item.getQuantity() : 0)
                    .totalAmount(o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                    .scheduledDate(o.getScheduledDate() != null ? o.getScheduledDate().toString() : null)
                    .status(o.getStatus())
                    .estimatedTime(null)
                    .note(null)
                    .createdAt(o.getCreatedAt() != null ? o.getCreatedAt().toString() : null)
                    .deliveredQty(delivery != null ? delivery.getDeliveredQty() : 0)
                    .emptyCollected(delivery != null ? delivery.getEmptyCollected() : 0)
                    .build();
        }).collect(Collectors.toList());
    }

    // ── PLACE ORDER ───────────────────────────────────────────────────────────

    @Transactional
    public CustomerOrderResponse placeOrder(Long customerId, Long vendorId, int quantity, String date, String note) {

        Customer customer = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new RuntimeException("Your account is not yet approved.");
        }

        Order order = new Order();
        order.setVendorId(vendorId);
        order.setCustomer(customer);
        order.setSource("CUSTOMER");
        order.setStatus("PENDING");
        order.setScheduledDate(LocalDate.parse(date));
        order.setTotalAmount(0.0);
        orderRepo.save(order);

        double price = customer.getPricePerCan() != null ? customer.getPricePerCan() : 0.0;
        OrderItem item = new OrderItem();
        item.setVendorId(vendorId);
        item.setOrder(order);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setTotal(quantity * price);
        itemRepo.save(item);

        order.setTotalAmount(item.getTotal());
        orderRepo.save(order);

        return CustomerOrderResponse.builder()
                .orderId(order.getId())
                .quantity(quantity)
                .totalAmount(item.getTotal())
                .scheduledDate(date)
                .status("PENDING")
                .createdAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : null)
                .deliveredQty(0)
                .emptyCollected(0)
                .build();
    }
}