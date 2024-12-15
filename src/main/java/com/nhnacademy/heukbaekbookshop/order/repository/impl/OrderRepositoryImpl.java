package com.nhnacademy.heukbaekbookshop.order.repository.impl;

import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderSearchCondition;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.memberset.customer.domain.QCustomer.customer;
import static com.nhnacademy.heukbaekbookshop.order.domain.QDelivery.delivery;
import static com.nhnacademy.heukbaekbookshop.order.domain.QDeliveryFee.deliveryFee;
import static com.nhnacademy.heukbaekbookshop.order.domain.QOrder.order;
import static com.nhnacademy.heukbaekbookshop.order.domain.QPayment.payment;
import static com.nhnacademy.heukbaekbookshop.order.domain.QPaymentType.paymentType;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Order> searchByOrderSearchCondition(OrderSearchCondition orderSearchCondition) {
        return Optional.ofNullable(queryFactory
                .selectFrom(order)
                .join(order.customer, customer).fetchJoin()
                .join(order.deliveryFee, deliveryFee).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .join(order.payment, payment).fetchJoin()
                .join(order.payment.paymentType, paymentType).fetchJoin()
                .where(
                        tossOrderIdEq(orderSearchCondition.tossOrderId()),
                        emailEq(orderSearchCondition.email())
                )
                .fetchOne());
    }

    private BooleanExpression tossOrderIdEq(String tossOrderId) {
        return tossOrderId == null ? null : order.tossOrderId.eq(tossOrderId);
    }

    private BooleanExpression emailEq(String email) {
        return email == null ? null : order.customerEmail.eq(email);
    }

    private BooleanExpression customerIdEq(Long customerId) {
        return customerId == null ? null : order.customer.id.eq(customerId);
    }

    @Override
    public Page<Order> searchAllByOrderSearchCondition(OrderSearchCondition orderSearchCondition,
                                                       Pageable pageable) {
        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.delivery, delivery).fetchJoin()
                .join(order.customer, customer).fetchJoin()
                .join(order.payment, payment).fetchJoin()
                .where(
                        customerIdEq(orderSearchCondition.customerId())
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(order.id.countDistinct())
                .from(order)
                .where(
                        customerIdEq(orderSearchCondition.customerId())
                )
                .orderBy(order.createdAt.desc())
                .fetchOne();

        return new PageImpl<>(orders, pageable, total);
    }
}