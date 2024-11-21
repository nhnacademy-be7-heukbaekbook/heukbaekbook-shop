package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;

public interface OrderService {

    Long createOrder(OrderCreateRequest orderCreateRequest);
}
