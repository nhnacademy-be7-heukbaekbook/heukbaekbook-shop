package com.nhnacademy.heukbaekbookshop.order.repository.impl;

import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import com.nhnacademy.heukbaekbookshop.order.repository.DeliveryFeeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.order.domain.QDeliveryFee.deliveryFee;

public class DeliveryFeeRepositoryImpl implements DeliveryFeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DeliveryFeeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<DeliveryFee> findByMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        return Optional.ofNullable(queryFactory
                .selectFrom(deliveryFee)
                .where(deliveryFee.minimumOrderAmount.loe(minimumOrderAmount))
                .orderBy(deliveryFee.minimumOrderAmount.desc())
                .fetchFirst());
    }
}
