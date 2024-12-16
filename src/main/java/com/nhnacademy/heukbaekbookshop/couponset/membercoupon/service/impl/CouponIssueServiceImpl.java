package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.mapper.CouponMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.exception.CouponNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.request.CouponIssueRequest;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.exception.MemberCouponAlreadyExistsException;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.repository.MemberCouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.CouponIssueService;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponIssueServiceImpl implements CouponIssueService {

    private static final String ISSUED_COUPON_KEY = "coupon:issue:%d";

    private final EntityManager entityManager;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberCouponResponse issueCouponSync(Long customerId, Long couponId) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(MemberNotFoundException::new);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("해당 ID의 쿠폰을 찾을 수 없습니다: " + couponId));

        if(memberCouponRepository.existsByMemberAndCoupon(member, coupon)) {
            throw new MemberCouponAlreadyExistsException();
        }

        LocalDateTime couponExpirationDate = getEarlierDateTime(LocalDateTime.now().plusDays(coupon.getAvailableDuration()), coupon.getCouponTimeEnd());
        MemberCoupon memberCoupon = CouponMapper.toMemberCouponEntity(member, coupon, couponExpirationDate);

        return CouponMapper.fromMemberCouponEntity(memberCouponRepository.save(memberCoupon));
    }

    @Override
    @Transactional
    public MemberCouponResponse issueCouponAsync(CouponIssueRequest couponIssueRequest) {
        MemberCoupon memberCoupon = createMemberCouponProxy(couponIssueRequest);
        return CouponMapper.fromMemberCouponEntity(memberCouponRepository.save(memberCoupon));
    }


    private MemberCoupon createMemberCouponProxy(CouponIssueRequest couponIssueRequest) {
        Member memberProxy = entityManager.getReference(Member.class, couponIssueRequest.customerId());
        Coupon couponProxy = entityManager.getReference(Coupon.class, couponIssueRequest.couponId());

        return MemberCoupon.builder()
                .member(memberProxy)
                .coupon(couponProxy)
                .expirationDate(couponIssueRequest.couponExpirationDate())
                .build();
    }


    private LocalDateTime getEarlierDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2) ? dateTime1 : dateTime2;
    }
}
