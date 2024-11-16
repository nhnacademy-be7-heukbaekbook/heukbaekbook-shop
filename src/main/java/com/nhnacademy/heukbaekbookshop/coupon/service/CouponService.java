package com.nhnacademy.heukbaekbookshop.coupon.service;

import com.nhnacademy.heukbaekbookshop.coupon.domain.CouponType;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.MemberCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nhnacademy.heukbaekbookshop.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.coupon.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.CouponInfoResponse;

public interface CouponService {
    // 쿠폰 조회 관련 메서드
//    CouponInfoResponse getCouponDetails(Long couponId); // 특정 쿠폰의 상세 정보 조회
    Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId);
    Page<CouponHistoryResponse> getCouponHistory(Pageable pageable, Long memberCouponId);
}