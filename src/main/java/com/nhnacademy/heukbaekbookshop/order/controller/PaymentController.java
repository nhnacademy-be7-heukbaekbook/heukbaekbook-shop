package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            response = new PaymentApprovalResponse("FAIL",false, "결제 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
