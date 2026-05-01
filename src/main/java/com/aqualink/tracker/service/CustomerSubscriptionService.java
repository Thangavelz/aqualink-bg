package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.response.CustomerSubscriptionResponse;
import com.aqualink.tracker.entity.Customer;
import com.aqualink.tracker.entity.Subscription;
import com.aqualink.tracker.repository.CustomerRepository;
import com.aqualink.tracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerSubscriptionService {

    private final SubscriptionRepository subRepo;
    private final CustomerRepository     customerRepo;

    /**
     * Maps frontend frequency label → delivery interval in days.
     * Also stores the raw string in a separate column (see Subscription entity fix).
     */
    private static final Map<String, Integer> FREQ_DAYS = Map.of(
            "DAILY",     1,
            "ALTERNATE", 2,
            "MWF",       2,   // approximation — scheduler handles exact days
            "WEEKLY",    7
    );

    // ─── GET ALL ─────────────────────────────────────────────────────────────

    public List<CustomerSubscriptionResponse> getAll(Long customerId, Long vendorId) {
        return subRepo.findByCustomerIdAndVendorId(customerId, vendorId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────

    @Transactional
    public CustomerSubscriptionResponse create(
            Long customerId, Long vendorId,
            int quantity, String frequency, String startDateStr
    ) {
        Customer customer = customerRepo.findByIdAndVendorId(customerId, vendorId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new RuntimeException("Your account is pending approval. You cannot create a subscription yet.");
        }

        int freqDays = FREQ_DAYS.getOrDefault(frequency.toUpperCase(), 1);
        LocalDate startDate = LocalDate.parse(startDateStr);

        Subscription sub = new Subscription();
        sub.setVendorId(vendorId);
        sub.setCustomer(customer);
        sub.setQuantity(quantity);
        sub.setFrequency(frequency.toUpperCase());   // stored as string
        sub.setFrequencyDays(freqDays);
        sub.setStartDate(startDate);
        sub.setNextRunDate(startDate);
        sub.setIsActive(true);

        subRepo.save(sub);
        return toDto(sub);
    }

    // ─── CANCEL ──────────────────────────────────────────────────────────────

    @Transactional
    public void cancel(Long customerId, Long vendorId, Long subId) {
        Subscription sub = subRepo.findById(subId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        // Security check — customer can only cancel their own subs
        if (!sub.getCustomer().getId().equals(customerId) || !sub.getVendorId().equals(vendorId)) {
            throw new RuntimeException("Unauthorized");
        }

        sub.setIsActive(false);
        subRepo.save(sub);
    }

    // ─── MAPPER ──────────────────────────────────────────────────────────────

    private CustomerSubscriptionResponse toDto(Subscription s) {
        return CustomerSubscriptionResponse.builder()
                .id(s.getId())
                .quantity(s.getQuantity())
                .frequency(s.getFrequency() != null ? s.getFrequency() : "DAILY")
                .startDate(s.getStartDate() != null ? s.getStartDate().toString() : "")
                .nextRunDate(s.getNextRunDate() != null ? s.getNextRunDate().toString() : "")
                .isActive(Boolean.TRUE.equals(s.getIsActive()))
                .build();
    }
}