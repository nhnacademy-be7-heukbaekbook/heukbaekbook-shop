package com.nhnacademy.heukbaekbookshop.memberset.customer.service;

import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;

public interface CustomerService {
    OrderDetailResponse getCustomerOrderDetails(String orderId, String email);
}
