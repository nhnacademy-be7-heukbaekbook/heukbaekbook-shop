package com.nhnacademy.heukbaekbookshop.order.service.template;

import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonalPaymentService extends AbstractPaymentApprovalService {

    private final EntityManager entityManager;

    public PersonalPaymentService(RefundRepository refundRepository,
                          CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          PaymentTypeRepository paymentTypeRepository,
                          OrderBookRefundRepository orderBookRefundRepository,
                          MemberRepository memberRepository,
                          ApplicationEventPublisher eventPublisher,
                          PaymentStrategyFactory paymentStrategyFactory,
                          EntityManager entityManager) {
        super(refundRepository, customerRepository, paymentRepository, orderRepository, paymentTypeRepository,
                orderBookRefundRepository, memberRepository, eventPublisher, paymentStrategyFactory);
        this.entityManager = entityManager;
    }

    // approvePayment는 AbstractPaymentApprovalService에서 제공

    public RefundCreateResponse refundPayment(RefundCreateRequest request) {
        String paymentMethod = request.method();
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);

        PaymentGatewayCancelResponse gatewayResponse = paymentStrategy.refundPayment(request);

        Order order = orderRepository.findByTossOrderId(gatewayResponse.orderId())
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));

        order.setStatus(OrderStatus.RETURNED);
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

    public PaymentCancelResponse cancelPayment(PaymentCancelRequest request) {
        String paymentMethod = request.method();
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);

        PaymentGatewayCancelResponse gatewayResponse = paymentStrategy.cancelPayment(request);

        Order order = orderRepository.findByTossOrderId(gatewayResponse.orderId())
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        return new PaymentCancelResponse("결제 취소 요청이 접수되었습니다.");
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
