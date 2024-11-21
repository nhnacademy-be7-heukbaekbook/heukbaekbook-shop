package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.impl;


import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QBookCoupon.bookCoupon;
import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QCategoryCoupon.categoryCoupon;
import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QCoupon.coupon;
import static com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.QCouponPolicy.couponPolicy;

public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CouponRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     *  인덱스가 존재하는 PK인 couponId로 Not Exists 조회하는 경우,
     *  모든 데이터를 로드하는 Left join 보다 성능이 효율적일 수 있음.
     *
     * @param pageable Page를 위한 Pageable 객체
     * @return 도서, 카테고리 쿠폰이 아닌 모든 쿠폰 반환
     */
    @Override
    public Page<Coupon> findAllNormalCoupons(Pageable pageable) {
        List<Coupon> content = queryFactory
                .selectFrom(coupon)
                .join(coupon.couponPolicy, couponPolicy).fetchJoin()
                .where(
                       JPAExpressions.selectFrom(bookCoupon)
                               .where(bookCoupon.id.eq(coupon.id))
                               .notExists(),
                        JPAExpressions.selectFrom(categoryCoupon)
                                .where(categoryCoupon.id.eq(coupon.id))
                                .notExists()
                )
                .orderBy(coupon.couponCreatedAt.desc())
                .fetch();

        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(
                        JPAExpressions.selectFrom(bookCoupon)
                                .where(bookCoupon.id.eq(coupon.id))
                                .notExists(),
                        JPAExpressions.selectFrom(categoryCoupon)
                                .where(categoryCoupon.id.eq(coupon.id))
                                .notExists()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<BookCouponResponse> findAllBookCoupons(Pageable pageable) {
        List<BookCouponResponse> content = queryFactory
                .select(Projections.constructor(BookCouponResponse.class,
                        bookCoupon.id,
                        bookCoupon.couponName,
                        bookCoupon.couponDescription,
                        bookCoupon.couponCreatedAt,
                        bookCoupon.couponStatus,
                        bookCoupon.availableDuration,
                        bookCoupon.couponTimeStart,
                        bookCoupon.couponTimeEnd,
                        bookCoupon.couponPolicy.discountType,
                        bookCoupon.couponPolicy.discountAmount,
                        bookCoupon.couponPolicy.minimumPurchaseAmount,
                        bookCoupon.couponPolicy.maximumPurchaseAmount,
                        bookCoupon.book.id,
                        bookCoupon.book.title
                ))
                .from(bookCoupon)
                .orderBy(bookCoupon.couponCreatedAt.desc())
                .fetch();

        long total = queryFactory
                .select(bookCoupon.count())
                .from(bookCoupon)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<CategoryCouponResponse> findAllCategoryCoupons(Pageable pageable) {
        List<CategoryCouponResponse> content = queryFactory
                .select(Projections.constructor(CategoryCouponResponse.class,
                        categoryCoupon.id,
                        categoryCoupon.couponName,
                        categoryCoupon.couponDescription,
                        categoryCoupon.couponCreatedAt,
                        categoryCoupon.couponStatus,
                        categoryCoupon.availableDuration,
                        categoryCoupon.couponTimeStart,
                        categoryCoupon.couponTimeEnd,
                        categoryCoupon.couponPolicy.discountType,
                        categoryCoupon.couponPolicy.discountAmount,
                        categoryCoupon.couponPolicy.minimumPurchaseAmount,
                        categoryCoupon.couponPolicy.maximumPurchaseAmount,
                        categoryCoupon.category.id,
                        categoryCoupon.category.name
                        ))
                .from(categoryCoupon)
                .orderBy(categoryCoupon.couponCreatedAt.desc())
                .fetch();

        long total = queryFactory
                .select(categoryCoupon.count())
                .from(categoryCoupon)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Coupon> findAllByCouponStatus(CouponStatus couponStatus, Pageable pageable) {
        List<Coupon> content = queryFactory
                .selectFrom(coupon)
                .join(coupon.couponPolicy, couponPolicy).fetchJoin()
                .where(coupon.couponStatus.eq(CouponStatus.ABLE))
                .orderBy(coupon.couponCreatedAt.desc())
                .fetch();

        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(coupon.couponStatus.eq(couponStatus))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<Coupon> findAllByDiscountType(DiscountType discountType, Pageable pageable) {
        List<Coupon> content = queryFactory
                .selectFrom(coupon)
                .join(coupon.couponPolicy, couponPolicy).fetchJoin()
                .where(coupon.couponPolicy.discountType.eq(discountType))
                .fetch();

        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(coupon.couponPolicy.discountType.eq(discountType))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
