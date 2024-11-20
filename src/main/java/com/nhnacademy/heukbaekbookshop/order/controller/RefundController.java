package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refund")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping("/{refund-id}")
    public ResponseEntity<RefundDetailResponse> getRefund(@PathVariable(name = "refund-id") Long refundId) {
        RefundDetailResponse refund = refundService.getRefund(refundId);
        return ResponseEntity.ok(refund);
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<List<RefundDetailResponse>> getRefunds(@PathVariable(name = "customer-id") Long customerId) {
        List<RefundDetailResponse> refunds = refundService.getRefunds(customerId);
        return ResponseEntity.ok(refunds);
    }

}
