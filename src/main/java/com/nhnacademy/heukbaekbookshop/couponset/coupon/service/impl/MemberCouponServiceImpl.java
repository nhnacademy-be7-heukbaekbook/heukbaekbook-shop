package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.exception.CouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.exception.MemberCouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.MemberCouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.MemberCouponService;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponHistoryRepository couponHistoryRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Override
    public MemberCouponResponse issueCoupon(Long memberId, Long couponId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found: " + couponId));

        MemberCoupon memberCoupon = CouponMapper.toMemberCouponEntity(member, coupon, coupon.getAvailableDuration());

        return CouponMapper.fromMemberCouponEntity(memberCouponRepository.save(memberCoupon));
    }

    @Override
    public MemberCouponResponse useCoupon(Long memberCouponId) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new MemberCouponNotFoundException("MemberCoupon not found: " + memberCouponId));

        if (memberCoupon.isCouponUsed()) {
            throw new IllegalStateException("Coupon is already used.");
        }

        // 쿠폰 사용 내역 저장
        CouponHistory couponHistory = new CouponHistory(
                null,
                memberCoupon,
                LocalDateTime.now(),
                new OrderBook()
        );

        couponHistoryRepository.save(couponHistory);

        MemberCoupon updatedCoupon = new MemberCoupon(
                memberCoupon.getId(),
                memberCoupon.getMember(),
                memberCoupon.getCoupon(),
                true,
                memberCoupon.getIssuedAt(),
                memberCoupon.getExpirationAt()
        );

        return CouponMapper.fromMemberCouponEntity(memberCouponRepository.save(updatedCoupon));
    }

    @Override
    public Page<MemberCouponResponse> getUserCoupons(Pageable pageable, Long memberId) {
        Page<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(pageable, memberId);
        return CouponMapper.fromMemberCouponPageableEntity(memberCoupons);
    }
}

