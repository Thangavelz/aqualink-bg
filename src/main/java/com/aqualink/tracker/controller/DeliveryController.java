package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.response.TodayDeliveryResponse;
import com.aqualink.tracker.repository.OrderRepository;
import com.aqualink.tracker.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderRepository orderRepo;

    /** GET /api/delivery/today?date=2026-05-01  (defaults to today) */
    @GetMapping("/today")
    public List<TodayDeliveryResponse> today(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @RequestParam(value = "date", required = false) String dateStr
    ) {
        LocalDate date = (dateStr != null && !dateStr.isBlank())
                ? LocalDate.parse(dateStr)
                : LocalDate.now();
        return orderRepo.findTodayDelivery(vendorId, date);
    }

    /**
     * GET /api/delivery/history?status=DELIVERED&from=2026-04-01&to=2026-05-01
     *
     * Uses separate repository methods per filter combo to avoid
     * PostgreSQL "could not determine data type of parameter" error
     * that occurs when passing NULL via JPQL ? IS NULL pattern.
     */
    @GetMapping("/history")
    public List<TodayDeliveryResponse> history(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "from",   required = false) String fromStr,
            @RequestParam(value = "to",     required = false) String toStr
    ) {
        // Default date range: last 30 days
        LocalDate from = (fromStr != null && !fromStr.isBlank())
                ? LocalDate.parse(fromStr)
                : LocalDate.now().minusDays(30);
        LocalDate to = (toStr != null && !toStr.isBlank())
                ? LocalDate.parse(toStr)
                : LocalDate.now();

        boolean hasStatus = status != null && !status.isBlank() && !"ALL".equals(status);

        if (hasStatus) {
            return orderRepo.findHistoryByStatusAndDateRange(vendorId, status, from, to);
        } else {
            return orderRepo.findHistoryByDateRange(vendorId, from, to);
        }
    }

    /** POST /api/delivery/deliver/{orderId}?deliveredQty=2&emptyCollected=1 */
    @PostMapping("/deliver/{orderId}")
    public void deliver(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("orderId")      Long orderId,
            @RequestParam("deliveredQty")   int deliveredQty,
            @RequestParam("emptyCollected") int emptyCollected
    ) {
        deliveryService.markDelivered(vendorId, orderId, deliveredQty, emptyCollected);
    }

    /** POST /api/delivery/skip/{orderId} */
    @PostMapping("/skip/{orderId}")
    public void skip(
            @RequestHeader("X-VENDOR-ID") Long vendorId,
            @PathVariable("orderId")      Long orderId
    ) {
        deliveryService.markSkipped(vendorId, orderId);
    }
}