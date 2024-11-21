package com.nhnacademy.heukbaekbookshop.order.service.impl;

import com.nhnacademy.heukbaekbookshop.common.service.CommonService;
import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderBookRepository orderBookRepository;

    private final CustomerRepository customerRepository;

    private final CommonService commonService;

    @Override
    @Transactional
    public Long createOrder(OrderCreateRequest orderCreateRequest) {
        Customer customer = Customer.createCustomer(
                orderCreateRequest.customerName(),
                orderCreateRequest.customerPhoneNumber(),
                orderCreateRequest.customerPhoneNumber());
        Customer savedCustomer = customerRepository.save(customer);

        Order order = Order.createOrder(
                commonService.convertStringToBigDecimal(orderCreateRequest.totalPrice()),
                orderCreateRequest.customerName(),
                orderCreateRequest.customerPhoneNumber(),
                orderCreateRequest.customerEmail(),
                orderCreateRequest.tossOrderId(),
                savedCustomer,
                new DeliveryFee()
        );

        Order savedOrder = orderRepository.save(order);

        return null;
    }
}