package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.UserBookCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponService {
    MemberCouponResponse useCoupon(Long memberCouponId, Long orderId, Long bookId);
    // 쿠폰 조회 관련 메서드
    Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId);
    Page<UserBookCouponResponse> getUserBookCouponsDownloadList(Long customerId, Long bookId, Pageable pageable);
}
