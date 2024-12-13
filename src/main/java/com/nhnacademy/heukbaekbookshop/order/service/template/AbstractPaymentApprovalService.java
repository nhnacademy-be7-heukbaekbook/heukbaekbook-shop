package com.nhnacademy.heukbaekbookshop.order.service.template;

import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import com.nhnacademy.heukbaekbookshop.point.history.event.OrderEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public abstract class AbstractPaymentApprovalService {
    protected final RefundRepository refundRepository;
    protected final PaymentRepository paymentRepository;
    protected final OrderRepository orderRepository;
    protected final PaymentTypeRepository paymentTypeRepository;
    protected final OrderBookRefundRepository orderBookRefundRepository;
    protected final MemberRepository memberRepository;
    protected final CustomerRepository customerRepository;
    protected final PaymentStrategyFactory paymentStrategyFactory;
    protected final ApplicationEventPublisher eventPublisher;

    protected AbstractPaymentApprovalService(RefundRepository refundRepository,
                                             CustomerRepository customerRepository,
                                             PaymentRepository paymentRepository,
                                             OrderRepository orderRepository,
                                             PaymentTypeRepository paymentTypeRepository,
                                             OrderBookRefundRepository orderBookRefundRepository,
                                             MemberRepository memberRepository,
                                             ApplicationEventPublisher eventPublisher,
                                             PaymentStrategyFactory paymentStrategyFactory) {
        this.refundRepository = refundRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.orderBookRefundRepository = orderBookRefundRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    /**
     * 템플릿 메서드 - 결제 승인 프로세스 전체 흐름 정의
     */
    public final PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        // 1. 결제 전략 획득
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(request.method());

        // 2. PG 결제 승인 호출
        PaymentGatewayApprovalResponse gatewayResponse = paymentStrategy.approvePayment(request);

        // 3. 주문 조회 및 검증
        Order order = findOrder(request.orderId());

        // 4. 결제타입 조회 또는 생성
        PaymentType paymentType = getOrCreatePaymentType(gatewayResponse.method());

        // 5. 결제 정보 생성 및 저장
        Payment payment = createAndSavePayment(order, paymentType, gatewayResponse);

        // 6. 주문 상태 업데이트
        updateOrderStatus(order, payment);

        // 7. 회원이면 포인트 적립 이벤트 발행
        publishOrderEventIfMember(order);

        // 8. 결제 승인 후 추가 작업
        afterPaymentApproved(order);

        // 8. 응답 생성
        return createApprovalResponse(order, payment);
    }

    protected Order findOrder(String tossOrderId) {
        return orderRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));
    }

    protected PaymentType getOrCreatePaymentType(String method) {
        return paymentTypeRepository.findByName(method)
                .orElseGet(() -> {
                    PaymentType newPaymentType = new PaymentType();
                    newPaymentType.setName(method);
                    return paymentTypeRepository.save(newPaymentType);
                });
    }

    protected Payment createAndSavePayment(Order order, PaymentType paymentType, PaymentGatewayApprovalResponse gatewayResponse) {
        Payment payment = Payment.builder()
                .id(gatewayResponse.paymentKey())
                .order(order)
                .paymentType(paymentType)
                .requestedAt(ZonedDateTime.parse(gatewayResponse.requestedAt()).toLocalDateTime())
                .approvedAt(ZonedDateTime.parse(gatewayResponse.approvedAt()).toLocalDateTime())
                .price(gatewayResponse.cancelAmount())
                .build();
        return paymentRepository.save(payment);
    }

    protected void updateOrderStatus(Order order, Payment payment) {
        order.setPayment(payment);
        order.setStatus(OrderStatus.PAYMENT_COMPLETED);
        orderRepository.save(order);
    }

    protected void publishOrderEventIfMember(Order order) {
        if (memberRepository.existsById(order.getCustomer().getId())) {
            BigDecimal productPrice = order.getTotalPrice().subtract(order.getDeliveryFee().getFee());
            eventPublisher.publishEvent(new OrderEvent(order.getCustomer().getId(), order.getId(), productPrice));
        }
    }

    protected PaymentApprovalResponse createApprovalResponse(Order order, Payment payment) {
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

    protected void afterPaymentApproved(Order order) {
        // 기본: 개인 고객 포인트 적립 이벤트 발행
        if (memberRepository.existsById(order.getCustomer().getId())) {
            BigDecimal productPrice = order.getTotalPrice().subtract(order.getDeliveryFee().getFee());
            eventPublisher.publishEvent(new OrderEvent(order.getCustomer().getId(), order.getId(), productPrice));
        }
    }
}
