package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentApprovalResponse> confirmPayment(@RequestBody PaymentApprovalRequest request) {
        PaymentApprovalResponse response = null;
        try {
            response = paymentService.approvePayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new PaymentApprovalResponse("결제에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/{payment-key}/cancel")
    public ResponseEntity<PaymentCancelResponse> cancelPayment(@PathVariable(name = "payment-key") String paymentKey, @RequestBody PaymentCancelRequest request) {
        PaymentCancelResponse response = null;
        try {
            response = paymentService.cancelPayment(paymentKey, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new PaymentCancelResponse("결제 취소 요청이 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{payment-id}")
    public ResponseEntity<PaymentDetailResponse> getPayment(@PathVariable(name = "payment-id") Long paymentId) {
        PaymentDetailResponse payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<List<PaymentDetailResponse>> getPayments(@PathVariable(name = "customer-id") Long customerId) {
        List<PaymentDetailResponse> payments = paymentService.getPayments(customerId);
        return ResponseEntity.ok(payments);
    }

}
