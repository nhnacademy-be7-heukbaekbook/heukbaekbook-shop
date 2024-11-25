package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;

import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<Order> searchByTossOrderId(String tossOrderId);
}
