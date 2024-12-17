package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import com.nhnacademy.heukbaekbookshop.point.history.event.OrderEvent;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentTypeRepository paymentTypeRepository;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private OrderBookRefundRepository orderBookRefundRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentService paymentService;

    private Refund existingRefund;
    private Order existingOrder;
    private OrderBook existingOrderBook;
    private OrderBookRefund existingOrderBookRefund;
    private Member existingMember;
    private Book existingBook;
    private PaymentType existingPaymentType;
    private Payment existingPayment;

    @BeforeEach
    void setUp() {
        // 기존 환불 내역 설정
        existingRefund = Refund.builder()
                .id(1L)
                .reason("상품 불량")
                .refundRequestAt(LocalDateTime.now().minusDays(2))
                .refundApprovedAt(LocalDateTime.now().minusDays(1))
                .refundStatus(RefundStatus.REQUEST)
                .build();

        // 기존 회원 설정
        existingMember = Member.builder()
                .name("John Doe")
                .loginId("johndoe")
                .password("password123")
                .birth(getSqlDate("1990-01-01")) // 수정된 부분
                .grade(new Grade())
                .build();
        setField(existingMember, "id", 10L);

        // 기존 주문 설정
        existingOrder = Order.createOrder(
                new BigDecimal("100.00"),
                "John Doe",
                "010-1234-5678",
                "johndoe@example.com",
                "TOSS123456",
                existingMember,
                new DeliveryFee()
        );
        setField(existingOrder, "id", 100L);

        // 기존 책 설정
        existingBook = new Book();
        existingBook.setTitle("Java Programming");
        setField(existingBook, "id", 200L);

        // 기존 주문서적 설정
        existingOrderBook = OrderBook.createOrderBook(
                existingBook.getId(),
                existingOrder.getId(),
                existingBook,
                existingOrder,
                2,
                new BigDecimal("50.00")
        );
        setField(existingOrderBook, "bookId", 200L);
        setField(existingOrderBook, "orderId", 100L);
        // setField(existingOrderBook, "id", 1000L); // 제거
        existingOrder.getOrderBooks().add(existingOrderBook);

        // 기존 주문서적 환불 설정
        existingOrderBookRefund = OrderBookRefund.builder()
                .bookId(existingBook.getId())
                .orderId(existingOrder.getId())
                .refundId(existingRefund.getId())
                .book(existingBook)
                .order(existingOrder)
                .refund(existingRefund)
                .quantity(2)
                .build();
        setField(existingOrderBookRefund, "bookId", 200L);
        setField(existingOrderBookRefund, "orderId", 100L);
        setField(existingOrderBookRefund, "refundId", 1L);
        // Note: OrderBookRefund has composite key, no single 'id' field

        // 기존 결제 타입 설정
        existingPaymentType = new PaymentType();
        setField(existingPaymentType, "id", 1L);
        existingPaymentType.setName("Credit Card");

        // 기존 결제 설정
        existingPayment = Payment.builder()
                .id("paymentKey123")
                .order(existingOrder)
                .paymentType(existingPaymentType)
                .requestedAt(ZonedDateTime.now().minusDays(1).toLocalDateTime())
                .approvedAt(ZonedDateTime.now().toLocalDateTime())
                .price(new BigDecimal("100.00"))
                .build();
        existingOrder.setPayment(existingPayment);
    }

    /**
     * Helper method to convert String to java.sql.Date
     */
    private java.sql.Date getSqlDate(String dateStr) {
        // Using LocalDate and converting to java.sql.Date
        LocalDate localDate = LocalDate.parse(dateStr);
        return java.sql.Date.valueOf(localDate);
    }

    // Helper method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Class<?> clazz = target.getClass();
            while (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(target, value);
                    return;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field '" + fieldName + "' on target.", e);
        }
    }

    @Test
    void testApprovePayment() {
        // Given
        PaymentApprovalRequest request = new PaymentApprovalRequest(
                "paymentKey123",
                "VALID_ORDER_ID",
                100L, // amount as BigDecimal
                "credit_card"
        );

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategyFactory.getStrategy("credit_card")).thenReturn(paymentStrategy);
        when(paymentStrategy.approvePayment(request)).thenReturn(new PaymentGatewayApprovalResponse(
                "paymentKey123",
                ZonedDateTime.now().minusDays(1).toString(),
                ZonedDateTime.now().toString(),
                new BigDecimal("0.00"),
                "Credit Card"
        ));

        PaymentType paymentType = new PaymentType();

        when(orderRepository.findByTossOrderId("VALID_ORDER_ID")).thenReturn(Optional.of(existingOrder));
        when(paymentTypeRepository.findByName("Credit Card")).thenReturn(Optional.of(existingPaymentType));

        //when
        PaymentApprovalResponse paymentApprovalResponse = paymentService.approvePayment(request);

        //then
        assertThat(paymentApprovalResponse).isNotNull();
    }

    @Test
    @DisplayName("approvePayment - 주문 정보를 찾을 수 없을 때 예외 발생")
    void testApprovePayment_OrderNotFound() {
        // Given
        PaymentApprovalRequest request = new PaymentApprovalRequest(
                "paymentKey123",
                "INVALID_ORDER_ID",
                100L, // amount as BigDecimal
                "credit_card"
        );

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategyFactory.getStrategy("credit_card")).thenReturn(paymentStrategy);
        when(paymentStrategy.approvePayment(request)).thenReturn(new PaymentGatewayApprovalResponse(
                "paymentKey123",
                ZonedDateTime.now().minusDays(1).toString(),
                ZonedDateTime.now().toString(),
                new BigDecimal("0.00"),
                "Credit Card"
        ));

        when(orderRepository.findByTossOrderId("INVALID_ORDER_ID")).thenReturn(Optional.empty());

        // When & Then
        PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
            paymentService.approvePayment(request);
        }, "존재하지 않는 주문 ID에 대해 PaymentFailureException이 발생해야 합니다.");

        assertEquals("주문 정보를 찾을 수 없습니다.", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
        verify(orderRepository, times(1)).findByTossOrderId("INVALID_ORDER_ID");
        verify(paymentTypeRepository, never()).findByName(anyString());
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(eventPublisher, never()).publishEvent(any(OrderEvent.class));
    }

    /**
     * refundPayment(RefundCreateRequest request) 메서드에 대한 테스트
     */
    @Test
    @DisplayName("refundPayment - 성공적으로 환불 요청 생성")
    void testRefundPayment_Success() {
        // Given
        RefundBookRequest refundBookRequest = new RefundBookRequest(200L, 2, new BigDecimal("100.00"));
        RefundCreateRequest request = new RefundCreateRequest(
                List.of(refundBookRequest),
                "paymentKey123",
                "상품 불량",
                "credit_card"
        );

        PaymentGatewayCancelResponse gatewayResponse = new PaymentGatewayCancelResponse(
                ZonedDateTime.now().minusDays(1).toString(),
                ZonedDateTime.now().toString(),
                "TOSS123456",
                new BigDecimal("100.00"),
                "Refund Successful"
        );

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategyFactory.getStrategy("credit_card")).thenReturn(paymentStrategy);
        when(paymentStrategy.refundPayment(request)).thenReturn(gatewayResponse);

        when(orderRepository.findByTossOrderId("TOSS123456")).thenReturn(Optional.of(existingOrder));
        when(refundRepository.save(any(Refund.class))).thenReturn(existingRefund);
        when(orderBookRefundRepository.save(any(OrderBookRefund.class))).thenReturn(existingOrderBookRefund);

        // When
        RefundCreateResponse response = paymentService.refundPayment(request);

        // Then
        verify(paymentStrategyFactory, times(1)).getStrategy("credit_card");
        verify(paymentStrategy, times(1)).refundPayment(request);
        verify(orderRepository, times(1)).findByTossOrderId("TOSS123456");
        verify(refundRepository, times(1)).save(any(Refund.class));
        verify(orderBookRefundRepository, times(1)).save(any(OrderBookRefund.class));
        verify(entityManager, times(1)).flush();

        assertNotNull(response, "RefundCreateResponse는 null이 아니어야 합니다.");
        assertEquals("환불 요청이 접수되었습니다.", response.message(), "메시지가 일치해야 합니다.");
    }

    @Test
    @DisplayName("refundPayment - 주문 정보를 찾을 수 없을 때 예외 발생")
    void testRefundPayment_OrderNotFound() {
        // Given
        RefundBookRequest refundBookRequest = new RefundBookRequest(200L, 2, new BigDecimal("100.00"));
        RefundCreateRequest request = new RefundCreateRequest(
                List.of(refundBookRequest),
                "paymentKey123",
                "상품 불량",
                "credit_card"
        );

        PaymentGatewayCancelResponse gatewayResponse = new PaymentGatewayCancelResponse(
                ZonedDateTime.now().minusDays(1).toString(),
                ZonedDateTime.now().toString(),
                "INVALID_ORDER_ID",
                new BigDecimal("100.00"),
                "Refund Successful"
        );

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategyFactory.getStrategy("credit_card")).thenReturn(paymentStrategy);
        when(paymentStrategy.refundPayment(request)).thenReturn(gatewayResponse);

        when(orderRepository.findByTossOrderId("INVALID_ORDER_ID")).thenReturn(Optional.empty());

        // When & Then
        PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
            paymentService.refundPayment(request);
        }, "존재하지 않는 주문 ID에 대해 PaymentFailureException이 발생해야 합니다.");

        assertEquals("주문 정보를 찾을 수 없습니다.", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
        verify(orderRepository, times(1)).findByTossOrderId("INVALID_ORDER_ID");
        verify(refundRepository, never()).save(any(Refund.class));
        verify(orderBookRefundRepository, never()).save(any(OrderBookRefund.class));
        verify(entityManager, never()).flush();
    }

    /**
     * cancelPayment(PaymentCancelRequest request) 메서드에 대한 테스트
     */
    @Test
    @DisplayName("cancelPayment - 성공적으로 결제 취소")
    void testCancelPayment_Success() {
        // Given
        PaymentCancelRequest request = new PaymentCancelRequest(
                "paymentKey123",
                "Refund Requested",
                "credit_card"
        );

        PaymentGatewayCancelResponse gatewayResponse = new PaymentGatewayCancelResponse(
                ZonedDateTime.now().minusDays(1).toString(),
                ZonedDateTime.now().toString(),
                "TOSS123456",
                new BigDecimal("100.00"),
                "Cancel Successful"
        );

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategyFactory.getStrategy("credit_card")).thenReturn(paymentStrategy);
        when(paymentStrategy.cancelPayment(request)).thenReturn(gatewayResponse);

        when(orderRepository.findByTossOrderId("TOSS123456")).thenReturn(Optional.of(existingOrder));

        // When
        PaymentCancelResponse response = paymentService.cancelPayment(request);

        // Then
        verify(paymentStrategyFactory, times(1)).getStrategy("credit_card");
        verify(paymentStrategy, times(1)).cancelPayment(request);
        verify(orderRepository, times(1)).findByTossOrderId("TOSS123456");
        verify(orderRepository, times(1)).save(existingOrder);

        assertNotNull(response, "PaymentCancelResponse는 null이 아니어야 합니다.");
        assertEquals("결제 취소 요청이 접수되었습니다.", response.message(), "메시지가 일치해야 합니다.");
        assertEquals(OrderStatus.CANCELED, existingOrder.getStatus(), "주문 상태가 'CANCELED'로 변경되어야 합니다.");
    }

    @Test
    @DisplayName("cancelPayment - 주문 정보를 찾을 수 없을 때 예외 발생")
    void testCancelPayment_OrderNotFound() {
        // Given
        PaymentCancelRequest request = new PaymentCancelRequest(
                "paymentKey123",
                "Refund Requested",
                "credit_card"
        );

        PaymentGatewayCancelResponse gatewayResponse = new PaymentGatewayCancelResponse(
                ZonedDateTime.now().minusDays(1).toString(),
                ZonedDateTime.now().toString(),
                "INVALID_ORDER_ID",
                new BigDecimal("100.00"),
                "Cancel Successful"
        );

        PaymentStrategy paymentStrategy = mock(PaymentStrategy.class);
        when(paymentStrategyFactory.getStrategy("credit_card")).thenReturn(paymentStrategy);
        when(paymentStrategy.cancelPayment(request)).thenReturn(gatewayResponse);

        when(orderRepository.findByTossOrderId("INVALID_ORDER_ID")).thenReturn(Optional.empty());

        // When & Then
        PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
            paymentService.cancelPayment(request);
        }, "존재하지 않는 주문 ID에 대해 PaymentFailureException이 발생해야 합니다.");

        assertEquals("주문 정보를 찾을 수 없습니다.", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
        verify(orderRepository, times(1)).findByTossOrderId("INVALID_ORDER_ID");
        verify(orderRepository, never()).save(any(Order.class));
    }

    /**
     * getPayments(Long customerId) 메서드에 대한 테스트
     */
    @Test
    @DisplayName("getPayments - 성공적으로 결제 내역 조회")
    void testGetPayments_Success() {
        // Given
        Long customerId = 10L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(List.of(existingOrder));
        when(paymentRepository.findByOrderId(existingOrder.getId())).thenReturn(existingPayment);

        // When
        List<PaymentDetailResponse> responses = paymentService.getPayments(customerId);

        // Then
        verify(customerRepository, times(1)).existsById(customerId);
        verify(orderRepository, times(1)).findByCustomerId(customerId);
        verify(paymentRepository, times(1)).findByOrderId(existingOrder.getId());

        assertNotNull(responses, "결제 내역 리스트는 null이 아니어야 합니다.");
        assertEquals(1, responses.size(), "결제 내역의 수가 일치해야 합니다.");

        PaymentDetailResponse response = responses.get(0);
        assertEquals(existingPayment.getId(), response.paymentId(), "결제 ID가 일치해야 합니다.");
        assertEquals("Credit Card", response.paymentType(), "결제 타입이 일치해야 합니다.");
        assertEquals(existingPayment.getRequestedAt().toString(), response.requestedAt(), "결제 요청일자가 일치해야 합니다.");
        assertEquals(existingPayment.getApprovedAt().toString(), response.approvedAt(), "결제 승인일자가 일치해야 합니다.");
        assertEquals(existingPayment.getPrice().longValue(), response.amount(), "결제 금액이 일치해야 합니다.");
    }

    @Test
    @DisplayName("getPayments - 고객 정보를 찾을 수 없을 때 예외 발생")
    void testGetPayments_CustomerNotFound() {
        // Given
        Long customerId = 999L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // When & Then
        PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
            paymentService.getPayments(customerId);
        }, "존재하지 않는 고객 ID에 대해 PaymentFailureException이 발생해야 합니다.");

        assertEquals("고객 정보를 찾을 수 없습니다.", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
        verify(customerRepository, times(1)).existsById(customerId);
        verify(orderRepository, never()).findByCustomerId(anyLong());
        verify(paymentRepository, never()).findByOrderId(anyLong());
    }

    /**
     * getPayment(String paymentId) 메서드에 대한 테스트
     */
    @Test
    @DisplayName("getPayment - 성공적으로 결제 상세 조회")
    void testGetPayment_Success() {
        // Given
        String paymentId = "paymentKey123";
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));

        // When
        PaymentDetailResponse response = paymentService.getPayment(paymentId);

        // Then
        verify(paymentRepository, times(1)).findById(paymentId);

        assertNotNull(response, "PaymentDetailResponse는 null이 아니어야 합니다.");
        assertEquals(existingPayment.getId(), response.paymentId(), "결제 ID가 일치해야 합니다.");
        assertEquals("Credit Card", response.paymentType(), "결제 타입이 일치해야 합니다.");
        assertEquals(existingPayment.getRequestedAt().toString(), response.requestedAt(), "결제 요청일자가 일치해야 합니다.");
        assertEquals(existingPayment.getApprovedAt().toString(), response.approvedAt(), "결제 승인일자가 일치해야 합니다.");
        assertEquals(existingPayment.getPrice().longValue(), response.amount(), "결제 금액이 일치해야 합니다.");
    }

    @Test
    @DisplayName("getPayment - 결제 정보를 찾을 수 없을 때 예외 발생")
    void testGetPayment_NotFound() {
        // Given
        String paymentId = "INVALID_PAYMENT_ID";
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // When & Then
        PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
            paymentService.getPayment(paymentId);
        }, "존재하지 않는 결제 ID에 대해 PaymentFailureException이 발생해야 합니다.");

        assertEquals("결제 정보를 찾을 수 없습니다.", exception.getMessage(), "예외 메시지가 일치해야 합니다.");
        verify(paymentRepository, times(1)).findById(paymentId);
    }
}