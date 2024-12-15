package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.common.util.Converter;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.exception.CustomerNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderSummaryResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.WrappingPaperNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.WrappingPaperRepository;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import com.nhnacademy.heukbaekbookshop.point.history.event.CancelEvent;
import com.nhnacademy.heukbaekbookshop.point.history.event.PointUseEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryFeeRepository deliveryFeeRepository;
    private final BookRepository bookRepository;
    private final DeliveryRepository deliveryRepository;
    private final EntityManager em;
    private final MemberRepository memberRepository;
    private final WrappingPaperRepository wrappingPaperRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Long createOrder(OrderCreateRequest orderCreateRequest) {
        DeliveryFee deliveryFee = deliveryFeeRepository.findByFee(new BigDecimal(orderCreateRequest.deliveryFee().replace(",", "")))
                .orElseThrow(() -> new DeliveryFeeNotFoundException("delivery fee not found"));

        Customer customer;
        if (orderCreateRequest.customerId() != null) {
            customer = customerRepository.findById(orderCreateRequest.customerId())
                    .orElseThrow(() -> new CustomerNotFoundException("customer not found"));
        } else {
            customer = Customer.createCustomer(
                    orderCreateRequest.customerName(),
                    orderCreateRequest.customerPhoneNumber(),
                    orderCreateRequest.customerEmail());
            customerRepository.save(customer);
        }

        Order order = Order.createOrder(
                Converter.convertStringToBigDecimal(orderCreateRequest.totalPrice()),
                orderCreateRequest.customerName(),
                orderCreateRequest.customerPhoneNumber(),
                orderCreateRequest.customerEmail(),
                orderCreateRequest.tossOrderId(),
                customer,
                deliveryFee
        );

        Order savedOrder = orderRepository.save(order);

        Delivery delivery = Delivery.createDelivery(
                savedOrder,
                orderCreateRequest.recipient(),
                orderCreateRequest.recipientPhoneNumber(),
                Long.valueOf(orderCreateRequest.postalCode()),
                orderCreateRequest.roadNameAddress(),
                orderCreateRequest.detailAddress(),
                null,
                null
        );

        deliveryRepository.save(delivery);

        Long orderId = savedOrder.getId();

        List<OrderBookRequest> orderBookRequests = orderCreateRequest.orderBookRequests();
        List<Long> bookIds = orderBookRequests.stream()
                .map(OrderBookRequest::bookId)
                .toList();

        // 주문한 모든 책을 쿼리 한방으로 조회
        bookRepository.findAllByBookSearchCondition(new BookSearchCondition(bookIds, null));

        orderBookRequests.forEach(
                orderBookRequest -> {
                    Long bookId = orderBookRequest.bookId();
                    // 미리 쿼리로 책을 다 가져와서 여기서 쿼리가 select 쿼리가 안나가도록 설계
                    Book book = bookRepository.findById(bookId)
                            .orElseThrow(() -> new BookNotFoundException(bookId));

                    OrderBook orderBook = OrderBook.createOrderBook(
                            bookId,
                            orderId,
                            book,
                            savedOrder,
                            orderBookRequest.quantity(),
                            Converter.convertStringToBigDecimal(orderBookRequest.salePrice())
                    );
                    em.persist(orderBook);

                    if (orderBookRequest.isWrapped()) {
                        Long wrappingPaperId = orderBookRequest.wrappingPaperId();
                        WrappingPaper wrappingPaper = wrappingPaperRepository.searchById(wrappingPaperId)
                                .orElseThrow(() -> new WrappingPaperNotFoundException(wrappingPaperId + " wrapping paper not found"));

                        Packaging packaging = Packaging.createPackaging(book, order, wrappingPaper, wrappingPaper.getPrice());
                        em.persist(packaging);
                    }
                });


        if (memberRepository.existsById(customer.getId())) {
            processPointUseEvent(orderCreateRequest, savedOrder.getId());
        }

        return orderId;
    }

    @Override
    public OrderDetailResponse getOrderDetailResponse(String tossOrderId) {
        Order order = orderRepository.searchByOrderSearchCondition(new OrderSearchCondition(tossOrderId, null, null))
                .orElseThrow(() -> new OrderNotFoundException(tossOrderId + " Order not found"));

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.of(order);

        log.info("orderDetailResponse: {}", orderDetailResponse);
        return orderDetailResponse;
    }


    @Override
    @Transactional
    public void deleteOrder(String tossOrderId) {
        Order order = orderRepository.findByTossOrderId(tossOrderId)
                .orElseThrow(() -> new OrderNotFoundException(tossOrderId + " Order not found"));

        order.setStatus(OrderStatus.CANCELED);

        if (memberRepository.existsById(order.getCustomer().getId())) {
            eventPublisher.publishEvent(new CancelEvent(order.getCustomer().getId(), order.getId()));
        }
    }

    @Override
    public OrderResponse getOrders(Pageable pageable) {
        Page<Order> result = orderRepository.searchAllByOrderSearchCondition(new OrderSearchCondition(null, null, null), pageable);
        
        return new OrderResponse(result.map(OrderSummaryResponse::of));
    }

    @Override
    @Transactional
    public void updateOrder(String orderId, OrderUpdateRequest orderUpdateRequest) {
        Order order = orderRepository.findByTossOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("tossOrderId:" + orderId + " Order not found"));

        OrderStatus orderStatus = OrderStatus.fromKorean(orderUpdateRequest.status());
        order.setStatus(orderStatus);
    }

    private static BigDecimal convertStringToBigDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String sanitizedValue = value.replaceAll("[^\\d.]", "");
            return new BigDecimal(sanitizedValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value, e);
        }
    }

    private void processPointUseEvent(OrderCreateRequest orderCreateRequest, Long orderId) {
        BigDecimal usedPoint = convertStringToBigDecimal(orderCreateRequest.usedPoint());

        if (usedPoint.compareTo(BigDecimal.ZERO) > 0) {
            eventPublisher.publishEvent(new PointUseEvent(orderCreateRequest.customerId(), orderId, usedPoint));
        }
    }
}