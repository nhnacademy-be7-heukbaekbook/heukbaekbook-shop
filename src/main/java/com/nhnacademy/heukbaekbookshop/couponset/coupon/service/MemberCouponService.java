package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.MemberCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponService {
    // 쿠폰 조회 관련 메서드
    Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId);
    Page<CouponHistoryResponse> getCouponHistory(Pageable pageable, Long memberCouponId);
}
