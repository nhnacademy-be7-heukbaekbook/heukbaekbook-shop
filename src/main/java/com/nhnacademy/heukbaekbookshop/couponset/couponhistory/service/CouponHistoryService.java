package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.service;

import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.request.CouponHistoryRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.response.CouponHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponHistoryService {
    void createCouponHistory(CouponHistoryRequest couponHistoryRequest);
    Page<CouponHistoryResponse> getCouponHistoryByCustomerId(Long memberId, Pageable pageable);
}
