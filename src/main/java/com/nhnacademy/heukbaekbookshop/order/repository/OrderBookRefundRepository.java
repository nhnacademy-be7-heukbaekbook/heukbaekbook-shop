package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookRefund;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookRefundPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderBookRefundRepository extends JpaRepository<OrderBookRefund, OrderBookRefundPK> {
    List<OrderBookRefund> findByRefundId(Long refundId);

    List<OrderBookRefund> findByOrderId(Long id);
}
