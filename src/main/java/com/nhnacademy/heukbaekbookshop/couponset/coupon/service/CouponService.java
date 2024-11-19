package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest couponRequest);
    CouponResponse getCoupon(Long couponId);
    Page<CouponResponse> getAllCoupons(Pageable pageable);
    CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest);
    void deleteCoupon(Long couponId);
}

// 전체 - order => created At
// 전체 - coupon status가 ABLE (진행중인 쿠폰)
// 전체 - couponPolicy type에 따라

