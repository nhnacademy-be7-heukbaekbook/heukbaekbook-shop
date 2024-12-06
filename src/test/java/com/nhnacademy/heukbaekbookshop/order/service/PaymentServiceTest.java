package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.*;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class PaymentServiceTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryFeeRepository deliveryFeeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    void approvePayment() {
        Customer customer = Customer.createCustomer(
                "name",
                "number",
                "wjdehdgus3892@gmail.com"
        );
        customerRepository.save(customer);

        DeliveryFee deliveryFee = new DeliveryFee("name", BigDecimal.ZERO, BigDecimal.ZERO);
        deliveryFeeRepository.save(deliveryFee);

        // 1. Order 저장
        Order order = Order.createOrder(
                BigDecimal.valueOf(10000),
                "customerName",
                "custmoerPhoneNumber",
                "customerEmail",
                "tossOrderId",
                customer,
                deliveryFee
        );
        orderRepository.save(order);

//        // 2. PaymentType 저장
        PaymentType paymentType = new PaymentType();
        paymentType.setName("name");
        paymentTypeRepository.save(paymentType);

//        // 3. Payment 생성 및 저장
        Payment payment = Payment.builder()
                .id("111")
//                .order(order)
                .paymentType(paymentType)
                .requestedAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .price(BigDecimal.valueOf(10000))
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        order.setPayment(savedPayment);
        log.info("payment : {}", order.getPayment().getClass());
        log.info("=====");
        entityManager.flush();
    }
}