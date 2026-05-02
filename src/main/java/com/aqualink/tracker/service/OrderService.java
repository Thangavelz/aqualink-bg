package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.*;
import com.aqualink.tracker.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRequestRepository requestRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;

    @Transactional
    public Order acceptRequest(Long vendorId, Long requestId, LocalDate deliveryDate) {

        OrderRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!req.getVendorId().equals(vendorId)) {
            throw new RuntimeException("Unauthorized");
        }

        if ("ACCEPTED".equals(req.getStatus())) {
            throw new RuntimeException("Request already accepted");
        }

        // FIX: was missing - status never persisted to DB
        req.setStatus("ACCEPTED");
        requestRepo.save(req);

        Order order = new Order();
        order.setVendorId(vendorId);
        order.setCustomer(req.getCustomer());
        order.setSource("REQUEST");
        order.setSourceId(req.getId());
        order.setStatus("PENDING");
        order.setScheduledDate(deliveryDate != null ? deliveryDate : req.getRequestedDate());

        order = orderRepo.save(order);

        double price = req.getCustomer().getPricePerCan() != null
                ? req.getCustomer().getPricePerCan()
                : 0.0;

        OrderItem item = new OrderItem();
        item.setVendorId(vendorId);
        item.setOrder(order);
        item.setProduct(null);
        item.setQuantity(req.getQuantity());
        item.setPrice(price);
        item.setTotal(req.getQuantity() * price);  // FIX: was Double * Double, null-safe now

        itemRepo.save(item);

        // Set totalAmount on the order now that we know the price
        order.setTotalAmount(item.getTotal());
        orderRepo.save(order);

        return order;
    }

    @Transactional
    public void rejectRequest(Long vendorId, Long requestId, String reason) {
        OrderRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!req.getVendorId().equals(vendorId)) {
            throw new RuntimeException("Unauthorized");
        }
        req.setStatus("REJECTED");
        req.setNotes(reason);
        requestRepo.save(req);
    }
}