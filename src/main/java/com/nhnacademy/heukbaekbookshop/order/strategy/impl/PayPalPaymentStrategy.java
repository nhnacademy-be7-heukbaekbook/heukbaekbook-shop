package com.nhnacademy.heukbaekbookshop.order.strategy.impl;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;

// @Component
public class PayPalPaymentStrategy implements PaymentStrategy {

    // PayPal 결제 전략 클래스. 예시 클래스임으로 실제로 동작하지 않음.
    @Override
    public String getPaymentMethodName() {
        return "PAYPAL";
    }

    @Override
    public PaymentGatewayApprovalResponse approvePayment(PaymentApprovalRequest request) {
        // PayPal 결제 승인 로직 구현
        // request 형식이 달라질 것임으로, 실제로는 구현되지 않음.
        return null;
    }

    @Override
    public PaymentGatewayCancelResponse refundPayment(RefundCreateRequest request) {
        // PayPal 환불 처리 로직 구현
        return null;
    }

    @Override
    public PaymentGatewayCancelResponse cancelPayment(PaymentCancelRequest request) {
        // PayPal 결제 취소 로직 구현
        return null;
    }
}
