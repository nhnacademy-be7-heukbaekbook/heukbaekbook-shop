package com.nhnacademy.heukbaekbookshop.order.strategy;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;

public interface PaymentStrategy {
    String getPaymentMethodName();

    PaymentGatewayApprovalResponse approvePayment(PaymentApprovalRequest request);

    PaymentGatewayCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request);
}
