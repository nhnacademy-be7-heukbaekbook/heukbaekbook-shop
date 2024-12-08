package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.common.util.Calculator;
import com.nhnacademy.heukbaekbookshop.common.util.Converter;
import com.nhnacademy.heukbaekbookshop.common.util.Formatter;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.exception.CustomerNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekbookshop.memberset.grade.dto.mapper.GradeMapper;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.WrappingPaperNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import com.nhnacademy.heukbaekbookshop.point.history.event.PointUseEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

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
    private final PaymentRepository paymentRepository;
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
        Order order = orderRepository.searchByTossOrderId(tossOrderId)
                .orElseThrow(() -> new OrderNotFoundException(tossOrderId + " Order not found"));

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.of(order);

        log.info("orderDetailResponse: {}", orderDetailResponse);
        return orderDetailResponse;
    }

    @Override
    public MyPageRefundableOrderDetailListResponse getRefundableOrders(String customerId) {
        Long customerIdL = Long.parseLong(customerId);
        LocalDateTime currentDate = LocalDateTime.now();

        List<Order> orderList = orderRepository.findByCustomerId(customerIdL);

        List<Order> refundableOrders = orderList.stream()
                .filter(order -> {
                    if (order.getStatus() != OrderStatus.DELIVERED) {
                        return false;
                    }
                    Delivery delivery = order.getDelivery();
                    if (delivery == null || delivery.getForwardingDate() == null) {
                        System.out.println("Delivery is null or Forwarding Date is null for Order ID: " + order.getId());
                        return false;
                    }
                    LocalDateTime forwardingDate = delivery.getForwardingDate().minusHours(9);

                    System.out.println("Order ID: " + order.getId() + ", Forwarding Date: " + forwardingDate);
                    System.out.println("Current Date: " + currentDate);

                    return !currentDate.isBefore(forwardingDate) && currentDate.isBefore(forwardingDate.plusDays(10));
                })
                .toList();

        // 주문 엔티티를 응답 DTO로 변환
        List<RefundableOrderDetailResponse> refundableOrderBookResponses = refundableOrders.stream()
                .map(order -> {
                    // 총 도서 가격 계산
                    BigDecimal totalBookPrice = order.getOrderBooks().stream()
                            .map(orderBook -> orderBook.getPrice().multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // 배송비
                    BigDecimal deliveryFee = order.getDeliveryFee() != null ? order.getDeliveryFee().getFee() : BigDecimal.ZERO;
                    String formattedDeliveryFee = Formatter.formatPrice(deliveryFee);

                    // 결제 정보
                    String paymentPrice = order.getPayment() != null ? Formatter.formatPrice(order.getPayment().getPrice()) : null;
                    String paymentTypeName = order.getPayment() != null ? order.getPayment().getPaymentType().getName() : null;

                    // 고객 및 배송 정보
                    String customerName = order.getCustomerName();
                    String recipient = order.getDelivery() != null ? order.getDelivery().getRecipient() : null;
                    Long postalCode = order.getDelivery() != null ? order.getDelivery().getPostalCode() : null;
                    String roadNameAddress = order.getDelivery() != null ? order.getDelivery().getRoadNameAddress() : null;
                    String detailAddress = order.getDelivery() != null ? order.getDelivery().getDetailAddress() : null;

                    // 총 할인 금액 계산
                    BigDecimal totalDiscount = order.getOrderBooks().stream()
                            .map(orderBook -> orderBook.getBook().getPrice()
                                    .subtract(orderBook.getPrice())
                                    .multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // 총 주문 금액 계산
                    String totalPrice = Formatter.formatPrice(totalBookPrice.add(deliveryFee));

                    // 주문 도서 목록 변환
                    List<RefundableOrderBookResponse> books = order.getOrderBooks().stream()
                            .map(this::mapToRefundableOrderBookResponse)
                            .collect(Collectors.toList());

                    // 추가 필드: orderId, createdAt, status
                    Long orderId = order.getId();
                    LocalDateTime createdAt = order.getCreatedAt();
                    String status = order.getStatus().name();

                    Payment payment = paymentRepository.findByOrderId(orderId);

                    // RefundableOrderDetailResponse 생성
                    return new RefundableOrderDetailResponse(
                            orderId,
                            customerName,
                            formattedDeliveryFee,
                            paymentPrice,
                            paymentTypeName,
                            recipient,
                            postalCode,
                            roadNameAddress,
                            detailAddress,
                            Formatter.formatPrice(totalBookPrice),
                            Formatter.formatPrice(totalDiscount),
                            totalPrice,
                            books,
                            createdAt,
                            status,
                            payment.getId()
                    );
                })
                .collect(Collectors.toList());

        GradeDto gradeDto = GradeMapper.createGradeResponse(memberRepository.findGradeByMemberId(customerIdL).orElseThrow(MemberNotFoundException::new));
        return new MyPageRefundableOrderDetailListResponse(gradeDto, refundableOrderBookResponses);

    }

    @Override
    public MyPageRefundableOrderDetailResponse getRefundableOrderDetail(String customerId, Long orderId) {
        Long customerIdL = Long.parseLong(customerId);

        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId + " Order not found"));

        // 총 도서 가격 계산
        BigDecimal totalBookPrice = order.getOrderBooks().stream()
                .map(orderBook -> orderBook.getPrice().multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 배송비 계산
        BigDecimal deliveryFee = order.getDeliveryFee() != null ? order.getDeliveryFee().getFee() : BigDecimal.ZERO;
        String formattedDeliveryFee = Formatter.formatPrice(deliveryFee);

        // 결제 정보
        String paymentPrice = order.getPayment() != null ? Formatter.formatPrice(order.getPayment().getPrice()) : null;
        String paymentTypeName = order.getPayment() != null ? order.getPayment().getPaymentType().getName() : null;

        // 고객 및 배송 정보
        String customerName = order.getCustomerName();
        String recipient = order.getDelivery() != null ? order.getDelivery().getRecipient() : null;
        Long postalCode = order.getDelivery() != null ? order.getDelivery().getPostalCode() : null;
        String roadNameAddress = order.getDelivery() != null ? order.getDelivery().getRoadNameAddress() : null;
        String detailAddress = order.getDelivery() != null ? order.getDelivery().getDetailAddress() : null;

        // 총 할인 금액 계산
        BigDecimal totalDiscount = order.getOrderBooks().stream()
                .map(orderBook -> orderBook.getBook().getPrice()
                        .subtract(orderBook.getPrice())
                        .multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 총 주문 금액 계산
        String totalPrice = Formatter.formatPrice(totalBookPrice.add(deliveryFee));

        // 주문 도서 목록 변환
        List<RefundableOrderBookResponse> books = order.getOrderBooks().stream()
                .map(this::mapToRefundableOrderBookResponse)
                .collect(Collectors.toList());

        // 추가 필드: orderId, createdAt, status
        Long orderIdValue = order.getId();
        LocalDateTime createdAt = order.getCreatedAt();
        String status = order.getStatus().name();

        // Payment 정보 조회
        Payment payment = paymentRepository.findByOrderId(orderIdValue);

        // 주문 상세 정보 생성
        RefundableOrderDetailResponse refundableOrderDetailResponse = new RefundableOrderDetailResponse(
                orderIdValue,
                customerName,
                formattedDeliveryFee,
                paymentPrice,
                paymentTypeName,
                recipient,
                postalCode,
                roadNameAddress,
                detailAddress,
                Formatter.formatPrice(totalBookPrice),
                Formatter.formatPrice(totalDiscount),
                totalPrice,
                books,
                createdAt,
                status,
                payment.getId()
        );

        // 회원 등급 정보
        GradeDto gradeDto = GradeMapper.createGradeResponse(
                memberRepository.findGradeByMemberId(customerIdL)
                        .orElseThrow(MemberNotFoundException::new)
        );

        return new MyPageRefundableOrderDetailResponse(gradeDto, refundableOrderDetailResponse);
    }


    private RefundableOrderBookResponse mapToRefundableOrderBookResponse(OrderBook orderBook) {
        Book book = orderBook.getBook();
        String thumbnailUrl = book.getBookImages().stream()
                .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                .map(Image::getUrl)
                .findFirst()
                .orElse("no-image");

        BigDecimal salePrice = Calculator.getSalePrice(book.getPrice(), book.getDiscountRate());

        return new RefundableOrderBookResponse(
                book.getId(),
                thumbnailUrl,
                book.getTitle(),
                Formatter.formatPrice(book.getPrice()),
                orderBook.getQuantity(),
                Formatter.formatPrice(salePrice),
                book.getDiscountRate(),
                Formatter.formatPrice(salePrice.multiply(BigDecimal.valueOf(orderBook.getQuantity())))
        );
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