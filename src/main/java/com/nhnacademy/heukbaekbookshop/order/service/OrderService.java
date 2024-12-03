package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundableOrderDetailListResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundableOrderDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    Long createOrder(OrderCreateRequest orderCreateRequest);

    OrderDetailResponse getOrderDetailResponse(String tossOrderId);

    MyPageRefundableOrderDetailListResponse getRefundableOrders(String customerId);

    MyPageRefundableOrderDetailResponse getRefundableOrderDetail(String customerId, Long orderId);

    ResponseEntity<Void> deleteOrder(String tossOrderId);
}
