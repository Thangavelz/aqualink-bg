package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.Customer;
import com.aqualink.tracker.entity.OrderRequest;
import com.aqualink.tracker.repository.CustomerRepository;
import com.aqualink.tracker.repository.OrderRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRequestService {

    private final OrderRequestRepository requestRepo;
    private final CustomerRepository customerRepo;

    public OrderRequest create(Long vendorId, Long customerId, int qty, LocalDate date) {

        Customer customer = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        OrderRequest r = new OrderRequest();
        r.setVendorId(vendorId);
        r.setCustomer(customer);
        r.setQuantity(qty);
        r.setRequestedDate(date);
        r.setStatus("PENDING");

        return requestRepo.save(r);
    }

    public List<OrderRequest> pending(Long vendorId) {
        return requestRepo.findByVendorIdAndStatus(vendorId, "PENDING");
    }
}