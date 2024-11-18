package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.MemberCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    // 쿠폰 조회 관련 메서드
//    CouponInfoResponse getCouponDetails(Long couponId); // 특정 쿠폰의 상세 정보 조회
    Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId);
    Page<CouponHistoryResponse> getCouponHistory(Pageable pageable, Long memberCouponId);
}