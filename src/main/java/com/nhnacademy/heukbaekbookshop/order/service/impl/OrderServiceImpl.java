package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.common.service.CommonService;
import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.exception.CustomerNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.mapper.MemberMapper;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final CommonService commonService;

    private final DeliveryFeeRepository deliveryFeeRepository;

    private final BookRepository bookRepository;

    private final DeliveryRepository deliveryRepository;

    private final EntityManager em;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

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
                commonService.convertStringToBigDecimal(orderCreateRequest.totalPrice()),
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
                            commonService.convertStringToBigDecimal(orderBookRequest.salePrice())
                    );
//                    orderBookRepository.save(orderBook);
                    em.persist(orderBook);
                });


        return orderId;
    }

    @Override
    public OrderDetailResponse getOrderDetailResponse(String tossOrderId) {
        Order order = orderRepository.searchByTossOrderId(tossOrderId)
                .orElseThrow(() -> new OrderNotFoundException(tossOrderId + " Order not found"));

        BigDecimal totalBookPrice = order.getOrderBooks().stream()
                .map(orderBook -> orderBook.getPrice().multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deliveryFee = order.getDeliveryFee().getFee();
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
                order.getCustomerName(),
                commonService.formatPrice(deliveryFee),
                commonService.formatPrice(order.getPayment().getPrice()),
                order.getPayment().getPaymentType().getName(),
                order.getDelivery().getRecipient(),
                order.getDelivery().getPostalCode(),
                order.getDelivery().getRoadNameAddress(),
                order.getDelivery().getDetailAddress(),
                commonService.formatPrice(totalBookPrice),
                commonService.formatPrice(order.getOrderBooks().stream()
                        .map(orderBook -> orderBook.getBook().getPrice().subtract(orderBook.getPrice()).multiply(BigDecimal.valueOf(orderBook.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)),
                commonService.formatPrice(totalBookPrice.add(deliveryFee)),
                order.getOrderBooks().stream()
                        .map(this::mapToOrderBookResponse)
                        .collect(Collectors.toList())
        );

        log.info("orderDetailResponse: {}", orderDetailResponse);
        return orderDetailResponse;
    }

    @Override
    public MyPageRefundableOrderDetailResponse getRefundableOrders(String userId) {
        Long customerId = Long.parseLong(userId);
        LocalDateTime currentDate = LocalDateTime.now();

        List<Order> orderList = orderRepository.findByCustomerId(customerId);

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
                    String formattedDeliveryFee = commonService.formatPrice(deliveryFee);

                    // 결제 정보
                    String paymentPrice = order.getPayment() != null ? commonService.formatPrice(order.getPayment().getPrice()) : null;
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
                    String totalPrice = commonService.formatPrice(totalBookPrice.add(deliveryFee));

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
                            commonService.formatPrice(totalBookPrice),
                            commonService.formatPrice(totalDiscount),
                            totalPrice,
                            books,
                            createdAt,
                            status,
                            payment.getId()
                    );
                })
                .collect(Collectors.toList());

        MemberResponse memberResponse = MemberMapper.createMemberResponse(memberRepository.findById(Long.parseLong(userId)).orElseThrow(MemberNotFoundException::new));
        return new MyPageRefundableOrderDetailResponse(memberResponse, refundableOrderBookResponses);

    }


    private OrderBookResponse mapToOrderBookResponse(OrderBook orderBook) {
        Book book = orderBook.getBook();
        String thumbnailUrl = book.getBookImages().stream()
                .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                .map(Image::getUrl)
                .findFirst()
                .orElse("no-image");

        BigDecimal salePrice = commonService.getSalePrice(book.getPrice(), book.getDiscountRate());

        return new OrderBookResponse(
                thumbnailUrl,
                book.getTitle(),
                commonService.formatPrice(book.getPrice()),
                orderBook.getQuantity(),
                commonService.formatPrice(salePrice),
                book.getDiscountRate(),
                commonService.formatPrice(salePrice.multiply(BigDecimal.valueOf(orderBook.getQuantity())))
        );
    }

    private RefundableOrderBookResponse mapToRefundableOrderBookResponse(OrderBook orderBook) {
        Book book = orderBook.getBook();
        String thumbnailUrl = book.getBookImages().stream()
                .filter(bookImage -> bookImage.getType() == ImageType.THUMBNAIL)
                .map(Image::getUrl)
                .findFirst()
                .orElse("no-image");

        BigDecimal salePrice = commonService.getSalePrice(book.getPrice(), book.getDiscountRate());

        return new RefundableOrderBookResponse(
                book.getId(),
                thumbnailUrl,
                book.getTitle(),
                commonService.formatPrice(book.getPrice()),
                orderBook.getQuantity(),
                commonService.formatPrice(salePrice),
                book.getDiscountRate(),
                commonService.formatPrice(salePrice.multiply(BigDecimal.valueOf(orderBook.getQuantity())))
        );
    }

}