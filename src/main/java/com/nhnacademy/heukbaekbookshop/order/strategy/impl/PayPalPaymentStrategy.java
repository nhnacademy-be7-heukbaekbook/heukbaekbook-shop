package com.nhnacademy.heukbaekbookshop.order.strategy.impl;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

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
        return new PaymentGatewayApprovalResponse(
                "paypalPaymentKey",
                "2023-10-01T10:00:00",
                "2023-10-01T10:01:00",
                request.amount().intValue(),
                "PAYPAL"
        );
    }

    @Override
    public PaymentGatewayCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
        // PayPal 결제 취소 로직 구현
        return new PaymentGatewayCancelResponse(
                "2023-10-02T10:00:00",
                "2023-10-02T10:01:00",
                "결제 취소 요청이 접수되었습니다."
        );
    }
}
