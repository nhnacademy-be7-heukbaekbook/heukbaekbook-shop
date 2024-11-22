package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchCondition;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.common.service.CommonService;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    @Transactional
    public Long createOrder(OrderCreateRequest orderCreateRequest) {
        DeliveryFee deliveryFee = deliveryFeeRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Invalid delivery fee"));

        Customer customer = Customer.createCustomer(
                orderCreateRequest.customerName(),
                orderCreateRequest.customerPhoneNumber(),
                orderCreateRequest.customerEmail());

        Customer savedCustomer = customerRepository.save(customer);

        Order order = Order.createOrder(
                commonService.convertStringToBigDecimal(orderCreateRequest.totalPrice()),
                orderCreateRequest.customerName(),
                orderCreateRequest.customerPhoneNumber(),
                orderCreateRequest.customerEmail(),
                orderCreateRequest.tossOrderId(),
                savedCustomer,
                deliveryFee
        );

        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        List<OrderBookRequest> orderBookRequests = orderCreateRequest.orderBookRequests();
        List<Long> bookIds = orderBookRequests.stream()
                .map(OrderBookRequest::bookId)
                .toList();

        bookRepository.findAllByBookSearchCondition(new BookSearchCondition(bookIds, null));
        log.info("=====");

        orderBookRequests.forEach(
                orderBookRequest -> {
                    Long bookId = orderBookRequest.bookId();
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
                    orderBookRepository.save(orderBook);
                });


        return orderId;
    }
}