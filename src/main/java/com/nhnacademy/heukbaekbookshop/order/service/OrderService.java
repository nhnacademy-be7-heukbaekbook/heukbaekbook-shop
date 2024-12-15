package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundableOrderDetailListResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundableOrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    Long createOrder(OrderCreateRequest orderCreateRequest);

    OrderDetailResponse getOrderDetailResponse(String tossOrderId);

    void deleteOrder(String tossOrderId);

    OrderResponse getOrders(Pageable pageable);

    void updateOrder(String orderId, OrderUpdateRequest orderUpdateRequest);
}
