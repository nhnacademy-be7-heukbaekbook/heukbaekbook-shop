package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalFailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentApprovalRequest request) {
        try {
            PaymentApprovalResponse response = paymentService.approvePayment(request);
            log.info("response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PaymentApprovalFailResponse("결제에 실패하였습니다."));
        }
    }

    @GetMapping("/{payment-id}")
    public ResponseEntity<PaymentDetailResponse> getPayment(@PathVariable(name = "payment-id") String paymentId) {
        PaymentDetailResponse payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<List<PaymentDetailResponse>> getPayments(@PathVariable(name = "customer-id") Long customerId) {
        List<PaymentDetailResponse> payments = paymentService.getPayments(customerId);
        return ResponseEntity.ok(payments);
    }
}
