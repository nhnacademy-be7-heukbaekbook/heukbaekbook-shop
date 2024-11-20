package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.MemberCouponRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    //    @Override
//    public CouponInfoResponse getCouponDetails(Long couponId) {
//        Coupon coupon = couponRepository.findById(couponId)
//                .orElseThrow(() -> new RuntimeException("Coupon not found"));
//        return CouponInfoResponse.from(coupon);
//    }

//    @Override
//    public Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId) {
//        Page<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(pageable, memberId);
//        return memberCoupons.map(MemberCouponResponse::from);
//    }


//
//
//    @Override
//    public Page<CouponHistoryResponse> getCouponHistory(Pageable pageable, Long memberCouponId) {
//        Page<CouponHistory> usageHistory = couponHistoryRepository.findByMemberCouponId(pageable, memberCouponId);
//        return usageHistory.map(CouponHistoryResponse::from);
//    }

}
