package com.nhnacademy.heukbaekbookshop.cart.repository.impl;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.repository.CartRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.cart.domain.QCart.*;

public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Cart> searchByCustomerId(Long customerId) {
        return queryFactory
                .selectFrom(cart)
                .fetch();
    }
}
