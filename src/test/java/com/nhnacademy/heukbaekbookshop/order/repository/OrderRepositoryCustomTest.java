package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Delivery;
import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Sql("order-test.sql")
class OrderRepositoryCustomTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryFeeRepository deliveryFeeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        DeliveryFee deliveryFee = deliveryFeeRepository.findById(1L)
                .orElseThrow(() -> new DeliveryFeeNotFoundException("delivery fee not found"));

        Customer customer = Customer.createCustomer("홍길동", "010-1234-5678", "wjdehdgus1234@gmail.com");
        customerRepository.save(customer);

        Order order = Order.createOrder(BigDecimal.valueOf(30000), "이순신", "010-1111-2222", "dltnstls@gmail.com", "1234", customer, deliveryFee);
        Order savedOrder = orderRepository.save(order);

        Delivery delivery = Delivery.createDelivery(savedOrder, "이상욱", "010-2222-1111", 25212L, "궁동 자연아파트", "201호", LocalDateTime.now(), LocalDateTime.now());
        deliveryRepository.save(delivery);

        Long bookId1 = 1L;
        Long bookId2 = 2L;

        Book book1 = bookRepository.findById(bookId1)
                .orElseThrow(() -> new BookNotFoundException(bookId1));

        Book book2 = bookRepository.findById(bookId2)
                .orElseThrow(() -> new BookNotFoundException(bookId2));

        OrderBook orderBook1 = OrderBook.createOrderBook(bookId1, order.getId(), book1, order, 1, BigDecimal.valueOf(15000));
        OrderBook orderBook2 = OrderBook.createOrderBook(bookId2, order.getId(), book2, order, 3, BigDecimal.valueOf(20000));

        em.persist(orderBook1);
        em.persist(orderBook2);

    }

    @Test
    void searchByOrderSearchCondition() {
        //given
        String tossOrderId = "1234";
        OrderSearchCondition orderSearchCondition = new OrderSearchCondition(tossOrderId, null, null);

        //when
        Optional<Order> result = orderRepository.searchByOrderSearchCondition(orderSearchCondition);

        //then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void searchAllByOrderSearchCondition() {
        //given
        String tossOrderId = "1234";
        OrderSearchCondition orderSearchCondition = new OrderSearchCondition(tossOrderId, null, null);
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<Order> orders = orderRepository.searchAllByOrderSearchCondition(orderSearchCondition, pageRequest);

        //then
        assertThat(orders.getTotalElements()).isEqualTo(1);
    }
}