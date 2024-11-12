package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 필요한 커스텀 메서드가 있다면 추가
}
