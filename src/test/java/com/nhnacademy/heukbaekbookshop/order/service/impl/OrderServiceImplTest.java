package com.nhnacademy.heukbaekbookshop.order.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepository;
import com.nhnacademy.heukbaekbookshop.point.history.event.CancelEvent;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DeliveryFeeRepository deliveryFeeRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private EntityManager em;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WrappingPaperRepository wrappingPaperRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_member() {
        // Given
        Book book = new Book();
        WrappingPaper wrappingPaper = new WrappingPaper();

        List<OrderBookRequest> orderBookRequests = List.of(
                new OrderBookRequest(1L, 5, "10000", true, 1L)
        );

        OrderCreateRequest request = new OrderCreateRequest(
                1L,                          // customerId
                "15000",                     // totalPrice
                "customer@example.com",      // customerEmail
                "John Doe",                  // customerName
                "123-456-7890",              // customerPhoneNumber
                "TOSS-12345",                // tossOrderId
                "Jane Doe",                  // recipient
                "12345",                     // postalCode
                "123 Main St",               // roadNameAddress
                "Apt 101",                   // detailAddress
                "987-654-3210",              // recipientPhoneNumber
                "500",                       // deliveryFee
                "1000",                      // usedPoint
                orderBookRequests            // orderBookRequests
        );

        DeliveryFee deliveryFee = new DeliveryFee("기본 배송료", new BigDecimal("500"), new BigDecimal("30000"));
        Customer customer = Customer.createCustomer("John Doe", "123-456-7890", "john@example.com");
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);

        when(deliveryFeeRepository.findByFee(new BigDecimal("500"))).thenReturn(Optional.of(deliveryFee));
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(wrappingPaperRepository.searchById(1L)).thenReturn(Optional.of(wrappingPaper));
        when(orderRepository.save(any())).thenReturn(order);

        // When
        Long orderId = orderService.createOrder(request);

        // Then
        assertNotNull(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void createOrder_non_member() {
        // Given
        Book book = new Book();
        WrappingPaper wrappingPaper = new WrappingPaper();

        List<OrderBookRequest> orderBookRequests = List.of(
                new OrderBookRequest(1L, 5, "10000", true, 1L)
        );

        OrderCreateRequest request = new OrderCreateRequest(
                null,                          // customerId
                "15000",                     // totalPrice
                "customer@example.com",      // customerEmail
                "John Doe",                  // customerName
                "123-456-7890",              // customerPhoneNumber
                "TOSS-12345",                // tossOrderId
                "Jane Doe",                  // recipient
                "12345",                     // postalCode
                "123 Main St",               // roadNameAddress
                "Apt 101",                   // detailAddress
                "987-654-3210",              // recipientPhoneNumber
                "500",                       // deliveryFee
                "1000",                      // usedPoint
                orderBookRequests            // orderBookRequests
        );

        DeliveryFee deliveryFee = new DeliveryFee("기본 배송료", new BigDecimal("500"), new BigDecimal("30000"));
        Customer customer = Customer.createCustomer("John Doe", "123-456-7890", "john@example.com");
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);

        when(deliveryFeeRepository.findByFee(new BigDecimal("500"))).thenReturn(Optional.of(deliveryFee));
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(wrappingPaperRepository.searchById(1L)).thenReturn(Optional.of(wrappingPaper));
        when(orderRepository.save(any())).thenReturn(order);

        // When
        Long orderId = orderService.createOrder(request);

        // Then
        assertNotNull(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void getOrderDetailResponse() {
        //given
        String tossOrderId = "1234";
        OrderSearchCondition orderSearchCondition = new OrderSearchCondition(tossOrderId, null, null);
        Customer customer = Customer.createCustomer(
                "홍길동",
                "010-1234-5678",
                "wjdehdgus@gmail.com"
        );

        DeliveryFee deliveryFee = new DeliveryFee("기본 배송료", BigDecimal.valueOf(5000), new BigDecimal("30000"));

        Order order = Order.createOrder(
                BigDecimal.valueOf(50000),
                "홍길동",
                "010-1234-5678",
                "wjdehdgus@gmail.com",
                tossOrderId,
                customer,
                deliveryFee
        );

        Delivery.createDelivery(
                order,
                "홍길동",
                "010-1234-5678",
                12345L,
                "road",
                "detail",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaymentType paymentType = new PaymentType(1L, "name");

        Payment payment = new Payment("paymentKey1", order, paymentType, LocalDateTime.now(), LocalDateTime.now(), BigDecimal.valueOf(50000));

        order.setPayment(payment);

        when(orderRepository.searchByOrderSearchCondition(orderSearchCondition)).thenReturn(Optional.of(order));

        //when
        OrderDetailResponse orderDetailResponse = orderService.getOrderDetailResponse(tossOrderId);

        //then
        assertThat(orderDetailResponse).isNotNull();
        Assertions.assertThat(orderDetailResponse.customerName()).isEqualTo(order.getCustomerName());

    }

    @Test
    void deleteOrder_ShouldMarkOrderAsCanceledAndPublishEvent() {
        // Given
        String tossOrderId = "ORD-123";
        Order order = mock(Order.class);
        Customer customer = mock(Customer.class);

        when(order.getCustomer()).thenReturn(customer);
        when(orderRepository.findByTossOrderId(tossOrderId)).thenReturn(Optional.of(order));
        when(customer.getId()).thenReturn(1L);
        when(memberRepository.existsById(1L)).thenReturn(true);

        // When
        orderService.deleteOrder(tossOrderId);

        // Then
        verify(order, times(1)).setStatus(OrderStatus.CANCELED);
        verify(eventPublisher, times(1)).publishEvent(any(CancelEvent.class));
    }

    @Test
    void updateOrder_ShouldUpdateOrderStatus() {
        // Given
        String orderId = "ORD-123";
        OrderUpdateRequest updateRequest = new OrderUpdateRequest("배송중");
        Customer customer = Customer.createCustomer("홍길동", "010-1234-5678", "wjdehdgus1234@gmail.com");
        DeliveryFee deliveryFee = new DeliveryFee(1L, "Express Delivery", BigDecimal.valueOf(5000), BigDecimal.valueOf(30000));

        Order order = Order.createOrder(BigDecimal.valueOf(30000), "이순신", "010-1111-2222", "dltnstls@gmail.com", "1234", customer, deliveryFee);


        when(orderRepository.findByTossOrderId(orderId)).thenReturn(Optional.of(order));

        // When
        orderService.updateOrder(orderId, updateRequest);

        // Then
        assertNotNull(order);
        assertEquals(OrderStatus.IN_TRANSIT, order.getStatus());
    }

    @Test
    void getOrders_ShouldReturnPaginatedOrderResponse() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> page = mock(Page.class);

        when(orderRepository.searchAllByOrderSearchCondition(any(OrderSearchCondition.class), eq(pageable)))
                .thenReturn(page);

        // When
        OrderResponse response = orderService.getOrders(pageable);

        // Then
        assertNotNull(response);
        verify(orderRepository, times(1))
                .searchAllByOrderSearchCondition(any(OrderSearchCondition.class), eq(pageable));
    }
}