package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.Customer;
import com.aqualink.tracker.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer create(Long vendorId, Customer c) {
        c.setVendorId(vendorId);
        return customerRepository.save(c);
    }

    public List<Customer> getAll(Long vendorId) {
        return customerRepository.findByVendorIdAndIsActiveTrue(vendorId);
    }

    public Customer get(Long vendorId, Long id) {
        return customerRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer update(Long vendorId, Long id, Customer input) {
        Customer c = get(vendorId, id);

        c.setName(input.getName());
        c.setPhone(input.getPhone());
        c.setAddress(input.getAddress());
        c.setPricePerCan(input.getPricePerCan());

        return customerRepository.save(c);
    }
}