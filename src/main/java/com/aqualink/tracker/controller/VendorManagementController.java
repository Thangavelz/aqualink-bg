package com.aqualink.tracker.controller;

import com.aqualink.tracker.dto.request.VendorOnboardRequest;
import com.aqualink.tracker.dto.response.VendorOnboardResponse;
import com.aqualink.tracker.service.VendorManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vendors")
@RequiredArgsConstructor
public class VendorManagementController {

    private final VendorManagementService svc;

    /** GET /api/admin/vendors — list all vendors */
    @GetMapping
    public ResponseEntity<List<VendorOnboardResponse>> list() {
        return ResponseEntity.ok(svc.listAll());
    }

    /** POST /api/admin/vendors — onboard a new vendor */
    @PostMapping
    public ResponseEntity<VendorOnboardResponse> onboard(@RequestBody VendorOnboardRequest req) {
        return ResponseEntity.ok(svc.onboard(req));
    }
}
