package com.nhnacademy.heukbaekbookshop.order.strategy.impl;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
public class PayPalPaymentStrategy implements PaymentStrategy {
    @Override
    public String getPaymentMethodName() {
        return "PAYPAL";
    }

    @Override
    public PaymentGatewayApprovalResponse approvePayment(PaymentApprovalRequest request) {
        // PayPal 결제 승인 로직 구현 (여기서는 Mock)
        ZonedDateTime now = ZonedDateTime.now();
        return new PaymentGatewayApprovalResponse(
                "paypal-paymentKey-123",
                now.minusSeconds(30).toString(),
                now.toString(),
                BigDecimal.valueOf(request.amount()),
                "PAYPAL"
        );
    }

    @Override
    public PaymentGatewayCancelResponse refundPayment(RefundCreateRequest request) {
        ZonedDateTime now = ZonedDateTime.now();
        BigDecimal totalRefundAmount = request.refundBooks().stream()
                .map(rb -> rb.price().multiply(BigDecimal.valueOf(rb.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PaymentGatewayCancelResponse(
                now.minusSeconds(30).toString(),
                now.toString(),
                request.paymentKey(),
                totalRefundAmount,
                "PAYPAL"
        );
    }

    @Override
    public PaymentGatewayCancelResponse cancelPayment(PaymentCancelRequest request) {
        ZonedDateTime now = ZonedDateTime.now();
        return new PaymentGatewayCancelResponse(
                now.minusSeconds(30).toString(),
                now.toString(),
                request.paymentKey(),
                BigDecimal.ZERO,
                "PAYPAL"
        );
    }
}
