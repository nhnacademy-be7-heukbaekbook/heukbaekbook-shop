package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponHistoryService {
    Page<CouponHistoryResponse> getCouponHistoryByCustomerId(Long memberId, Pageable pageable);
}
