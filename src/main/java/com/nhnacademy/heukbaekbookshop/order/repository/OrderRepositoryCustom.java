package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<Order> searchByOrderSearchCondition(OrderSearchCondition orderSearchCondition);

    Page<Order> searchAllByOrderSearchCondition(OrderSearchCondition orderSearchCondition,
                                         Pageable pageable);
}
