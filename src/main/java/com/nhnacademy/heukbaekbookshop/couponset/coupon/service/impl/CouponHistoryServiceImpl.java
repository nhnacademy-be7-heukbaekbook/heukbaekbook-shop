package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponHistoryMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {
    private final CouponHistoryRepository couponHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CouponHistoryResponse> getCouponHistoryByCustomerId(Long memberId, Pageable pageable) {
        // 사용자 ID 기준으로 쿠폰 사용 내역 조회 후 DTO로 변환
        return couponHistoryRepository.findByMemberCoupon_Member_Id(pageable, memberId)
                .map(CouponHistoryMapper::toResponse);
    }
}
