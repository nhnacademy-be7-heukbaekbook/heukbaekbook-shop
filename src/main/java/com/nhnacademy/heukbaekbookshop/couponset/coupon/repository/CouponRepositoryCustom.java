package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CouponRepositoryCustom {
    Page<Coupon> findAllNormalCoupons(Pageable pageable);

    Page<BookCouponResponse> findAllBookCoupons(Pageable pageable);

    Page<CategoryCouponResponse> findAllCategoryCoupons(Pageable pageable);

    Page<Coupon> findAllByCouponStatus(CouponStatus couponStatus, Pageable pageable);

    Page<Coupon> findAllByDiscountType(DiscountType discountType, Pageable pageable);

    Optional<Long> findAvailableCouponIdByCouponType(CouponType couponType);

    List<Coupon> findDownloadableCouponsByBookId(Long bookId);
}
