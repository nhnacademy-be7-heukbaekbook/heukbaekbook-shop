package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponHistoryRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponHistoryService {
    void createCouponHistory(CouponHistoryRequest couponHistoryRequest);
    Page<CouponHistoryResponse> getCouponHistoryByCustomerId(Long memberId, Pageable pageable);
}
