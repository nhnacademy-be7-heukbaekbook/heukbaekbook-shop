package com.nhnacademy.heukbaekbookshop.couponset.coupon.service;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.MemberCouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CouponHistoryRepository couponHistoryRepository;


    public CouponServiceImpl(CouponRepository couponRepository, MemberCouponRepository memberCouponRepository, CouponHistoryRepository couponHistoryRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

//    @Override
//    public CouponInfoResponse getCouponDetails(Long couponId) {
//        Coupon coupon = couponRepository.findById(couponId)
//                .orElseThrow(() -> new RuntimeException("Coupon not found"));
//        return CouponInfoResponse.from(coupon);
//    }

    @Override
    public Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId) {
        Page<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(pageable, memberId);
        return memberCoupons.map(MemberCouponResponse::from);
    }




    @Override
    public Page<CouponHistoryResponse> getCouponHistory(Pageable pageable, Long memberCouponId) {
        Page<CouponHistory> usageHistory = couponHistoryRepository.findByMemberCouponId(pageable, memberCouponId);
        return usageHistory.map(CouponHistoryResponse::from);
    }
}
