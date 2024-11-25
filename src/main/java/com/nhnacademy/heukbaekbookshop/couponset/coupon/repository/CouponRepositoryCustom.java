package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {
    Page<Coupon> findAllNormalCoupons(Pageable pageable);

    Page<BookCouponResponse> findAllBookCoupons(Pageable pageable);

    Page<CategoryCouponResponse> findAllCategoryCoupons(Pageable pageable);

    Page<Coupon> findAllByCouponStatus(CouponStatus couponStatus, Pageable pageable);

    Page<Coupon> findAllByDiscountType(DiscountType discountType, Pageable pageable);

}
