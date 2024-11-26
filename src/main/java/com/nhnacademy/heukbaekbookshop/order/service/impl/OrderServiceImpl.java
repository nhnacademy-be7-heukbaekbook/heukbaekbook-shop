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
import com.nhnacademy.heukbaekbookshop.order.domain.Delivery;
import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderBookResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderBookRepository orderBookRepository;

    private final CustomerRepository customerRepository;

    private final CommonService commonService;

    private final DeliveryFeeRepository deliveryFeeRepository;

    private final BookRepository bookRepository;

    private final DeliveryRepository deliveryRepository;

    private final EntityManager em;

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
}