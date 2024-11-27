package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
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

    @GetMapping
    public ResponseEntity<MyPageRefundDetailResponse> getRefunds(@RequestParam String customerId) {
        MyPageRefundDetailResponse refunds = refundService.getRefunds(customerId);
        return ResponseEntity.ok(refunds);
    }

    @PostMapping
    public ResponseEntity<RefundCreateResponse> requestRefund(@RequestBody RefundCreateRequest request) {
        RefundCreateResponse response = refundService.requestRefund(request);
        return ResponseEntity.ok(response);
    }
}
