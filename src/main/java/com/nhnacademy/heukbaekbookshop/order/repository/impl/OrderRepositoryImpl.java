package com.nhnacademy.heukbaekbookshop.order.repository.impl;

import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.memberset.customer.domain.QCustomer.*;
import static com.nhnacademy.heukbaekbookshop.order.domain.QDelivery.*;
import static com.nhnacademy.heukbaekbookshop.order.domain.QDeliveryFee.*;
import static com.nhnacademy.heukbaekbookshop.order.domain.QOrder.*;
import static com.nhnacademy.heukbaekbookshop.order.domain.QPayment.*;
import static com.nhnacademy.heukbaekbookshop.order.domain.QPaymentType.*;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Order> searchByTossOrderId(String tossOrderId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(order)
                .join(order.customer, customer).fetchJoin()
                .join(order.deliveryFee, deliveryFee).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .join(order.payment, payment).fetchJoin()
                .join(order.payment.paymentType, paymentType).fetchJoin()
                .where(order.tossOrderId.eq(tossOrderId))
                .fetchOne());
    }

    @Override
    public List<Order> searchByCustomerId(Long customerId) {
        return queryFactory
                .selectFrom(order)
                .join(order.delivery, delivery).fetchJoin()
                .join(order.customer, customer)
                .where(order.customer.id.eq(customerId))
                .orderBy(order.createdAt.desc())
                .fetch();
    }
}
