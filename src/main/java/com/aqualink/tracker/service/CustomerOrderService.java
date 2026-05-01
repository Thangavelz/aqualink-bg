package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.response.CustomerOrderResponse;
import com.aqualink.tracker.entity.*;
import com.aqualink.tracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final OrderRepository        orderRepo;
    private final OrderItemRepository    itemRepo;
    private final DeliveryRepository     deliveryRepo;
    private final CustomerRepository     customerRepo;
    private final OrderRequestRepository requestRepo;

    // ─── GET HISTORY ─────────────────────────────────────────────────────────

    public List<CustomerOrderResponse> getHistory(Long customerId, Long vendorId) {
        return orderRepo
                .findByCustomerIdAndVendorIdOrderByScheduledDateDesc(customerId, vendorId)
                .stream()
                .map(o -> toDto(o))
                .collect(Collectors.toList());
    }

    // ─── PLACE ORDER ─────────────────────────────────────────────────────────

    @Transactional
    public CustomerOrderResponse placeOrder(Long customerId, Long vendorId, int quantity, String dateStr, String note) {

        Customer customer = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new RuntimeException("Your account is pending approval. You cannot place orders yet.");
        }

        LocalDate scheduledDate = LocalDate.parse(dateStr);

        // 1. Create OrderRequest
        OrderRequest req = new OrderRequest();
        req.setVendorId(vendorId);
        req.setCustomer(customer);
        req.setQuantity(quantity);
        req.setRequestedDate(scheduledDate);
        req.setStatus("ACCEPTED");
        requestRepo.save(req);

        // 2. Create Order
        double pricePerCan = customer.getPricePerCan() != null ? customer.getPricePerCan() : 0.0;

        Order order = new Order();
        order.setVendorId(vendorId);
        order.setCustomer(customer);
        order.setSource("CUSTOMER_APP");
        order.setSourceId(req.getId());
        order.setStatus("PENDING");
        order.setScheduledDate(scheduledDate);
        order.setTotalAmount(quantity * pricePerCan);
        orderRepo.save(order);

        // 3. Create OrderItem
        OrderItem item = new OrderItem();
        item.setVendorId(vendorId);
        item.setOrder(order);
        item.setQuantity(quantity);
        item.setPrice(pricePerCan);
        item.setTotal(quantity * pricePerCan);
        itemRepo.save(item);

        return toDto(order);
    }

    // ─── MAPPER ──────────────────────────────────────────────────────────────

    private CustomerOrderResponse toDto(Order o) {
        // Quantity from OrderItem (first item — V1 is single-product)
        List<OrderItem> items = itemRepo.findByOrderId(o.getId());
        int qty = items.isEmpty() ? 0 : items.get(0).getQuantity();

        // Delivery details if present
        var delivery = deliveryRepo.findByOrderId(o.getId());
        int deliveredQty    = delivery.map(Delivery::getDeliveredQty).orElse(0);
        int emptyCollected  = delivery.map(Delivery::getEmptyCollected).orElse(0);

        return CustomerOrderResponse.builder()
                .orderId(o.getId())
                .quantity(qty)
                .totalAmount(o.getTotalAmount())
                .scheduledDate(o.getScheduledDate().toString())
                .status(o.getStatus())
                .estimatedTime(null)   // wire FCM/ETA later
                .note(null)            // wire note field to Order entity when needed
                .createdAt(o.getCreatedAt().toString())
                .deliveredQty(deliveredQty)
                .emptyCollected(emptyCollected)
                .build();
    }
}
