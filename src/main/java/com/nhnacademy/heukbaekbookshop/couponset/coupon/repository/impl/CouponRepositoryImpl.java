package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.impl;


import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepositoryCustom;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.book.domain.QBookCategory.bookCategory;
import static com.nhnacademy.heukbaekbookshop.category.domain.QCategory.category;
import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QBookCoupon.bookCoupon;
import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QCategoryCoupon.categoryCoupon;
import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QCoupon.coupon;
import static com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.QCouponPolicy.couponPolicy;

public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final List<CouponStatus> couponStatusOrders = List.of(CouponStatus.ABLE, CouponStatus.DISABLE);

    OrderSpecifier<Integer> orderByCouponStatus = new CaseBuilder()
            .when(coupon.couponStatus.eq(CouponStatus.ABLE)).then(0)
            .when(coupon.couponStatus.eq(CouponStatus.DISABLE)).then(1)
            .otherwise(2)
            .asc();

    // bookCoupon 엔티티의 경우
    OrderSpecifier<Integer> orderByBookCouponStatus = new CaseBuilder()
            .when(bookCoupon.couponStatus.eq(CouponStatus.ABLE)).then(0)
            .when(bookCoupon.couponStatus.eq(CouponStatus.DISABLE)).then(1)
            .otherwise(2)
            .asc();

    // categoryCoupon 엔티티의 경우
    OrderSpecifier<Integer> orderByCategoryCouponStatus = new CaseBuilder()
            .when(categoryCoupon.couponStatus.eq(CouponStatus.ABLE)).then(0)
            .when(categoryCoupon.couponStatus.eq(CouponStatus.DISABLE)).then(1)
            .otherwise(2)
            .asc();


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
                       JPAExpressions.selectOne()
                               .from(bookCoupon)
                               .where(bookCoupon.id.eq(coupon.id))
                               .notExists(),
                        JPAExpressions.selectOne()
                                .from(categoryCoupon)
                                .where(categoryCoupon.id.eq(coupon.id))
                                .notExists()
                )
                .orderBy(orderByCouponStatus,
                        coupon.couponCreatedAt.desc(),
                        coupon.couponPolicy.discountAmount.asc())
                .fetch();

        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(
                        JPAExpressions.selectOne()
                                .from(bookCoupon)
                                .where(bookCoupon.id.eq(coupon.id))
                                .notExists(),
                        JPAExpressions.selectOne()
                                .from(categoryCoupon)
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
                        bookCoupon.couponQuantity,
                        bookCoupon.availableDuration,
                        bookCoupon.couponTimeStart,
                        bookCoupon.couponTimeEnd,
                        bookCoupon.couponType,
                        bookCoupon.couponPolicy.id,
                        bookCoupon.couponPolicy.discountType,
                        bookCoupon.couponPolicy.discountAmount,
                        bookCoupon.couponPolicy.minimumPurchaseAmount,
                        bookCoupon.couponPolicy.maximumPurchaseAmount,
                        bookCoupon.book.id,
                        bookCoupon.book.title
                ))
                .from(bookCoupon)
                .orderBy(orderByBookCouponStatus, bookCoupon.couponCreatedAt.desc(), bookCoupon.couponPolicy.discountAmount.asc())
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
                        categoryCoupon.couponQuantity,
                        categoryCoupon.availableDuration,
                        categoryCoupon.couponTimeStart,
                        categoryCoupon.couponTimeEnd,
                        categoryCoupon.couponType,
                        categoryCoupon.couponPolicy.id,
                        categoryCoupon.couponPolicy.discountType,
                        categoryCoupon.couponPolicy.discountAmount,
                        categoryCoupon.couponPolicy.minimumPurchaseAmount,
                        categoryCoupon.couponPolicy.maximumPurchaseAmount,
                        categoryCoupon.category.id,
                        categoryCoupon.category.name
                        ))
                .from(categoryCoupon)
                .orderBy(orderByCategoryCouponStatus, categoryCoupon.couponCreatedAt.desc(), categoryCoupon.couponPolicy.discountAmount.asc())
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
                .where(coupon.couponStatus.eq(couponStatus))
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
                .orderBy(orderByCouponStatus, coupon.couponCreatedAt.desc(), coupon.couponPolicy.discountAmount.asc())
                .fetch();

        long total = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(coupon.couponPolicy.discountType.eq(discountType))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<Long> findAvailableCouponIdByCouponType(CouponType couponType) {
        return Optional.ofNullable(queryFactory
                .select(coupon.id)
                .from(coupon)
                .where(coupon.couponType.eq(couponType),
                        coupon.couponStatus.eq(CouponStatus.ABLE))
                .fetchOne()
        );
    }

    @Override
    public List<Coupon> findDownloadableCouponsByBookId(Long bookId) {
        // 1. 책에 직접 연결된 쿠폰 가져오기
        List<Coupon> bookCoupons = queryFactory
                .select(Projections.constructor(Coupon.class,
                        bookCoupon.id,
                        bookCoupon.couponPolicy,
                        bookCoupon.couponQuantity,
                        bookCoupon.couponStatus,
                        bookCoupon.availableDuration,
                        bookCoupon.couponTimeStart,
                        bookCoupon.couponTimeEnd,
                        bookCoupon.couponName,
                        bookCoupon.couponDescription,
                        bookCoupon.couponCreatedAt,
                        bookCoupon.couponType))
                .from(bookCoupon)
                .join(bookCoupon.couponPolicy, couponPolicy)
                .where(
                        bookCoupon.book.id.eq(bookId),
                        bookCoupon.couponStatus.eq(CouponStatus.ABLE),
                        bookCoupon.couponTimeStart.loe(LocalDateTime.now()),
                        bookCoupon.couponTimeEnd.goe(LocalDateTime.now())
                )
                .fetch();

        // 2. 책의 카테고리 가져오기
        List<Long> bookCategoryIds = queryFactory
                .select(bookCategory.category.id)
                .from(bookCategory)
                .where(bookCategory.book.id.eq(bookId))
                .fetch();

        // 3. 카테고리 및 상위 카테고리 가져오기
        List<Long> allCategoryIds = new ArrayList<>(bookCategoryIds);

        for (Long categoryId : bookCategoryIds) {
            fetchParentCategories(categoryId, allCategoryIds);
        }

        // 4. 카테고리 쿠폰 가져오기
        List<Coupon> categoryCoupons = queryFactory
                .select(Projections.constructor(Coupon.class,
                        categoryCoupon.id,
                        categoryCoupon.couponPolicy,
                        categoryCoupon.couponQuantity,
                        categoryCoupon.couponStatus,
                        categoryCoupon.availableDuration,
                        categoryCoupon.couponTimeStart,
                        categoryCoupon.couponTimeEnd,
                        categoryCoupon.couponName,
                        categoryCoupon.couponDescription,
                        categoryCoupon.couponCreatedAt,
                        categoryCoupon.couponType))
                .from(categoryCoupon)
                .join(categoryCoupon.couponPolicy, couponPolicy)
                .where(
                        categoryCoupon.category.id.in(allCategoryIds),
                        categoryCoupon.couponStatus.eq(CouponStatus.ABLE),
                        categoryCoupon.couponTimeStart.loe(LocalDateTime.now()),
                        categoryCoupon.couponTimeEnd.goe(LocalDateTime.now())
                )
                .fetch();

        // 5. 일반 쿠폰 가져오기
        List<Coupon> generalCoupons = queryFactory
                .select(coupon)
                .from(coupon)
                .join(coupon.couponPolicy, couponPolicy).fetchJoin()
                .where(
                        coupon.couponType.eq(CouponType.GENERAL),
                        coupon.couponStatus.eq(CouponStatus.ABLE),
                        coupon.couponTimeStart.loe(LocalDateTime.now()),
                        coupon.couponTimeEnd.goe(LocalDateTime.now())
                )
                .fetch();

        // 6. 모든 쿠폰 병합
        List<Coupon> allCoupons = new ArrayList<>();
        allCoupons.addAll(bookCoupons);
        allCoupons.addAll(categoryCoupons);
        allCoupons.addAll(generalCoupons);

        return allCoupons;
    }

    private void fetchParentCategories(Long categoryId, List<Long> allCategoryIds) {
        Long parentCategoryId = queryFactory
                .select(category.parentCategory.id)
                .from(category)
                .where(category.id.eq(categoryId))
                .fetchOne();

        if (parentCategoryId != null && !allCategoryIds.contains(parentCategoryId)) {
            allCategoryIds.add(parentCategoryId);
            fetchParentCategories(parentCategoryId, allCategoryIds);
        }
    }
}
