package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QCoupon.coupon;
import static com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.QCouponPolicy.couponPolicy;

public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CouponRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Coupon> findAllByPageable(Pageable pageable) {

        List<Coupon> content = queryFactory
                .selectFrom(coupon)
                .join(coupon.couponPolicy, couponPolicy).fetchJoin()
                .orderBy(coupon.couponCreatedAt.desc())
                .fetch();

        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .fetchOne();

        return new PageImpl<>(content, pageable,total);
    }

    @Override
    public Page<Coupon> findAllByCouponStatus(CouponStatus couponStatus, Pageable pageable) {
        return null;
    }


    @Override
    public Page<Coupon> findAllByDiscountType(DiscountType disCountType, Pageable pageable) {
        return null;
    }
}
