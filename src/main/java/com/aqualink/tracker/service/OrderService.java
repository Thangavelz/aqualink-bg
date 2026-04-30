package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.*;
import com.aqualink.tracker.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRequestRepository requestRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;

    @Transactional
    public Order acceptRequest(Long vendorId, Long requestId) {

        OrderRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!req.getVendorId().equals(vendorId)) {
            throw new RuntimeException("Unauthorized");
        }

        req.setStatus("ACCEPTED");

        Order order = new Order();
        order.setVendorId(vendorId);
        order.setCustomer(req.getCustomer());
        order.setSource("REQUEST");
        order.setSourceId(req.getId());
        order.setStatus("PENDING");
        order.setScheduledDate(req.getRequestedDate());

        order = orderRepo.save(order);

        // Create 1 item (water can)
        OrderItem item = new OrderItem();
        item.setVendorId(vendorId);
        item.setOrder(order);
        item.setProduct(null); // V1: optional
        item.setQuantity(req.getQuantity());
        item.setPrice(req.getCustomer().getPricePerCan());
        item.setTotal(item.getQuantity() * item.getPrice());

        itemRepo.save(item);

        return order;
    }
}