package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.request.CouponIssueRequest;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.repository.MemberCouponRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponIssueServiceImplTest {

    @InjectMocks
    private CouponIssueServiceImpl couponIssueService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private EntityManager em;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Test
    void issueCouponSync() {
        //given
        Long customerId = 1L;
        Long couponId = 1L;

        Grade grade = Grade.builder()
                .build();

        Member member = Member.builder()
                .grade(grade)
                .build();

        CouponPolicy couponPolicy = CouponPolicy.builder()
                .build();

        Coupon coupon = Coupon.builder()
                .couponPolicy(couponPolicy)
                .availableDuration(7)
                .couponTimeEnd(LocalDateTime.now().plusDays(7))
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .member(member)
                .coupon(coupon)
                .build();

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(memberCouponRepository.existsByMemberAndCoupon(member, coupon)).thenReturn(false);
        when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

        //when
        MemberCouponResponse memberCouponResponse = couponIssueService.issueCouponSync(customerId, couponId);

        //then
        assertThat(memberCouponResponse).isNotNull();
        assertThat(memberCouponResponse.isCouponUsed()).isFalse();
    }

    @Test
    void issueCouponAsync() {
        //given
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(
                1L,
                1L,
                LocalDateTime.now().plusDays(7)
        );

        Grade grade = Grade.builder()
                .build();

        Member member = Member.builder()
                .grade(grade)
                .build();

        CouponPolicy couponPolicy = CouponPolicy.builder()
                .build();

        Coupon coupon = Coupon.builder()
                .couponPolicy(couponPolicy)
                .availableDuration(7)
                .couponTimeEnd(LocalDateTime.now().plusDays(7))
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .member(member)
                .coupon(coupon)
                .build();

        when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(memberCoupon);

        //when
        MemberCouponResponse memberCouponResponse = couponIssueService.issueCouponAsync(couponIssueRequest);

        //then
        assertThat(memberCouponResponse).isNotNull();
        assertThat(memberCouponResponse.isCouponUsed()).isFalse();
    }
}