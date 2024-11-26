package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderSummaryResponse;

import java.util.List;

public interface OrderService {

    Long createOrder(OrderCreateRequest orderCreateRequest);

    OrderDetailResponse getOrderDetailResponse(String tossOrderId);
}
