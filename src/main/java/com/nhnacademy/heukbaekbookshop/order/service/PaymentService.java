package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RefundRepository refundRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final EntityManager entityManager;
    private final OrderBookRefundRepository orderBookRefundRepository;

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

        Payment payment = Payment.builder()
                .id(gatewayResponse.paymentKey())
                .order(order)
                .paymentType(paymentType)
                .requestedAt(ZonedDateTime.parse(gatewayResponse.requestedAt()).toLocalDateTime())
                .approvedAt(ZonedDateTime.parse(gatewayResponse.approvedAt()).toLocalDateTime())
                .price(gatewayResponse.cancelAmount())
                .build();


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

    public RefundCreateResponse cancelPayment(RefundCreateRequest request) {
        String paymentMethod = request.method();

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);

        PaymentGatewayCancelResponse gatewayResponse = paymentStrategy.cancelPayment(request);

        Order order = orderRepository.findByTossOrderId(gatewayResponse.orderId())
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));

        if (order.getTotalPrice().equals(gatewayResponse.cancelAmount())) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            order.setStatus(OrderStatus.REFUNDED);
        }

        orderRepository.save(order);

        Refund refund = Refund.builder()
                .reason(request.cancelReason())
                .refundRequestAt(ZonedDateTime.parse(gatewayResponse.requestedAt()).toLocalDateTime())
                .refundApprovedAt(ZonedDateTime.parse(gatewayResponse.approvedAt()).toLocalDateTime())
                .refundStatus(RefundStatus.REQUEST)
                .build();

        refundRepository.save(refund);

        entityManager.flush();

        for (RefundBookRequest refundBookRequest : request.refundBooks()) {
            OrderBookRefund orderBookRefund = OrderBookRefund.builder()
                    .bookId(refundBookRequest.bookId())
                    .orderId(order.getId())
                    .refundId(refund.getId())
                    .quantity(refundBookRequest.quantity())
                    .build();
            orderBookRefundRepository.save(orderBookRefund);
        }

        return new RefundCreateResponse("환불 요청이 접수되었습니다.");
    }

    public List<PaymentDetailResponse> getPayments(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new PaymentFailureException("고객 정보를 찾을 수 없습니다.");
        }
        return orderRepository.findByCustomerId(customerId).stream()
                .map(order -> paymentRepository.findByOrderId(order.getId()))
                .filter(Objects::nonNull)
                .map(payment -> new PaymentDetailResponse(
                        payment.getId(),
                        payment.getPaymentType().getName(),
                        payment.getRequestedAt().toString(),
                        payment.getApprovedAt() != null ? payment.getApprovedAt().toString() : null,
                        payment.getPrice().longValue()))
                .collect(Collectors.toList());
    }


    public PaymentDetailResponse getPayment(String paymentId) {
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
