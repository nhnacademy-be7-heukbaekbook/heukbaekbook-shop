package com.nhnacademy.heukbaekbookshop.memberset.customer.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.customer.service.CustomerService;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDetailResponse getCustomerOrderDetails(String orderId, String email) {
        Order order = orderRepository.searchByOrderSearchCondition(new OrderSearchCondition(orderId, email, null))
                .orElseThrow(() -> new OrderNotFoundException("해당 주문번호와 이메일에 대한 주문이 존재하지 않습니다."));

        return OrderDetailResponse.of(order);
    }
}
