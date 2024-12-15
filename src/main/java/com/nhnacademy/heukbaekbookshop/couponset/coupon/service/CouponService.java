package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest couponRequest);
    CouponResponse getCoupon(Long couponId);
    List<CouponResponse> getDownloadableCoupons(Long bookId);
    Page<CouponResponse> getAllNormalCoupons(Pageable pageable);
    Page<BookCouponResponse> getAllBookCoupons(Pageable pageable);
    Page<CategoryCouponResponse> getAllCategoryCoupons(Pageable pageable);
    Page<CouponResponse> getCouponsByType(DiscountType discountType, Pageable pageable);
    Page<CouponResponse> getCouponsByStatus(CouponStatus couponStatus, Pageable pageable);
    CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest);
    void changeCouponStatus(Long couponId, CouponStatus couponStatus);

    CouponPageResponse getCouponPageResponse(Long customerId, Pageable pageable);

    Long getCouponIdByCouponType(CouponType couponType);
}

// 전체 - order => created At
// 전체 - coupon status가 ABLE (진행중인 쿠폰)
// 전체 - couponPolicy type에 따라

