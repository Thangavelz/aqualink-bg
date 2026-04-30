package com.aqualink.tracker.service;

import com.aqualink.tracker.dto.response.TodayDeliveryResponse;
import com.aqualink.tracker.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodayDeliveryService {

    private final OrderRepository orderRepository;

    public List<TodayDeliveryResponse> getToday(Long vendorId) {
        return orderRepository.findTodayDelivery(vendorId, LocalDate.now());
    }
}