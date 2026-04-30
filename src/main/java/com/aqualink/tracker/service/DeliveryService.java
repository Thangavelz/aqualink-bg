package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.*;
import com.aqualink.tracker.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final LedgerRepository ledgerRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Mark order as delivered
     */
    @Transactional
    public void markDelivered(
            Long vendorId,
            Long orderId,
            int deliveredQty,
            int emptyCollected
    ) {

        // 1️⃣ Fetch Order (vendor safe)
        Order order = orderRepository.findByIdAndVendorId(orderId, vendorId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Prevent duplicate delivery
        deliveryRepository.findByOrderId(orderId).ifPresent(d -> {
            throw new RuntimeException("Already delivered");
        });

        Customer customer = order.getCustomer();

        // 3️⃣ Calculate amount
        double pricePerCan = customer.getPricePerCan() != null
                ? customer.getPricePerCan()
                : 0.0;

        double totalAmount = deliveredQty * pricePerCan;

        // 4️⃣ Update Order
        order.setStatus("DELIVERED");
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // 5️⃣ Create Delivery record
        Delivery delivery = new Delivery();
        delivery.setVendorId(vendorId);
        delivery.setOrder(order);
        delivery.setDeliveredQty(deliveredQty);
        delivery.setEmptyCollected(emptyCollected);
        delivery.setStatus("DELIVERED");
        delivery.setDeliveredAt(LocalDateTime.now());

        deliveryRepository.save(delivery);

        // 6️⃣ Ledger Entry (DEBIT)
        LedgerEntry ledger = new LedgerEntry();
        ledger.setVendorId(vendorId);
        ledger.setCustomer(customer);
        ledger.setType("DEBIT");
        ledger.setAmount(totalAmount);
        ledger.setReferenceType("ORDER");
        ledger.setReferenceId(order.getId());

        ledgerRepository.save(ledger);

        // 7️⃣ Update Customer (CRITICAL)
        int newCanBalance = (customer.getCanBalance() != null ? customer.getCanBalance() : 0)
                + deliveredQty - emptyCollected;

        double newDue = (customer.getDueAmount() != null ? customer.getDueAmount() : 0.0)
                + totalAmount;

        customer.setCanBalance(newCanBalance);
        customer.setDueAmount(newDue);

        customerRepository.save(customer);
    }

    /**
     * Mark order as skipped
     */
    @Transactional
    public void markSkipped(Long vendorId, Long orderId) {

        Order order = orderRepository.findByIdAndVendorId(orderId, vendorId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("DELIVERED".equals(order.getStatus())) {
            throw new RuntimeException("Already delivered, cannot skip");
        }

        order.setStatus("SKIPPED");
        orderRepository.save(order);

        // Optional: create delivery record with status SKIPPED
        Delivery delivery = new Delivery();
        delivery.setVendorId(vendorId);
        delivery.setOrder(order);
        delivery.setDeliveredQty(0);
        delivery.setEmptyCollected(0);
        delivery.setStatus("SKIPPED");
        delivery.setDeliveredAt(LocalDateTime.now());

        deliveryRepository.save(delivery);
    }
}