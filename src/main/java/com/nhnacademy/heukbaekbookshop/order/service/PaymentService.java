package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.PaymentRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.PaymentTypeRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.RefundRepository;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final RefundRepository refundRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentService(RefundRepository refundRepository,
                          CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          PaymentTypeRepository paymentTypeRepository,
                          PaymentStrategyFactory paymentStrategyFactory) {
        this.refundRepository = refundRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    public PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        String paymentMethod = request.method();

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);

        PaymentGatewayApprovalResponse gatewayResponse = paymentStrategy.approvePayment(request);

        Order order = orderRepository.findByTossOrderId(request.orderId())
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));

        PaymentType paymentType = paymentTypeRepository.findByName(gatewayResponse.method())
                .orElseGet(() -> {
                    PaymentType newPaymentType = new PaymentType();
                    newPaymentType.setName(gatewayResponse.method());
                    return paymentTypeRepository.save(newPaymentType);
                });

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentType(paymentType);
        payment.setRequestedAt(LocalDateTime.parse(gatewayResponse.requestedAt()));
        payment.setApprovedAt(LocalDateTime.parse(gatewayResponse.approvedAt()));
        payment.setPrice(BigDecimal.valueOf(gatewayResponse.totalAmount()));
        payment.setPaymentKey(gatewayResponse.paymentKey());

        order.setStatus(OrderStatus.PAYMENT_COMPLETED);

        paymentRepository.save(payment);

        return new PaymentApprovalResponse(
                order.getCreatedAt().toString(),
                order.getStatus().name(),
                order.getCustomerName(),
                order.getCustomerPhoneNumber(),
                order.getCustomerEmail(),
                payment.getPaymentType().getName(),
                payment.getRequestedAt().toString(),
                payment.getApprovedAt().toString(),
                payment.getPrice().intValue(),
                "결제에 성공하였습니다."
        );
    }

    public PaymentCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
        String paymentMethod = request.method();

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);

        PaymentGatewayCancelResponse gatewayResponse = paymentStrategy.cancelPayment(paymentKey, request);

        Refund refund = new Refund();
        refund.setReason(request.cancelReason());
        refund.setRefundRequestAt(LocalDateTime.parse(gatewayResponse.requestedAt()));
        refund.setRefundApprovedAt(LocalDateTime.parse(gatewayResponse.approvedAt()));
        refund.setRefundStatus(RefundStatus.REQUEST);

        refundRepository.save(refund);

        return new PaymentCancelResponse(gatewayResponse.message());
    }

    public List<PaymentDetailResponse> getPayments(Long customerId) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new PaymentFailureException("고객 정보를 찾을 수 없습니다.");
        }

        return orderRepository.findByCustomerId(customerId).stream()
                .flatMap(order -> paymentRepository.findByOrderId(order.getId()).stream())
                .map(payment -> new PaymentDetailResponse(
                        payment.getId(),
                        payment.getPaymentType().getName(),
                        payment.getRequestedAt().toString(),
                        payment.getApprovedAt().toString(),
                        payment.getPrice().longValue()))
                .collect(Collectors.toList());
    }

    public PaymentDetailResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentFailureException("결제 정보를 찾을 수 없습니다."));

        return new PaymentDetailResponse(
                payment.getId(),
                payment.getPaymentType().getName(),
                payment.getRequestedAt().toString(),
                payment.getApprovedAt().toString(),
                payment.getPrice().longValue());
    }
}
