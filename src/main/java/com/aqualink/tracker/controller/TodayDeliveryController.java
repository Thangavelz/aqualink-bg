package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.response.TodayDeliveryResponse;
import com.aqualink.tracker.service.TodayDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class TodayDeliveryController {

    private final TodayDeliveryService service;

    @GetMapping("/today")
    public List<TodayDeliveryResponse> getToday(
            @RequestHeader("X-VENDOR-ID") Long vendorId
    ) {
        return service.getToday(vendorId);
    }
}