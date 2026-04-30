package com.aqualink.tracker.service;

import com.aqualink.tracker.entity.Subscription;
import com.aqualink.tracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repo;

    public List<Subscription> dueToday(Long vendorId) {
        return repo.findByVendorIdAndNextRunDateLessThanEqual(vendorId, LocalDate.now());
    }
}