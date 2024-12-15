package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.BookCouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.repository.MemberCouponRepository;
import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookId;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberCouponServiceImplTest {

    @InjectMocks
    private MemberCouponServiceImpl memberCouponService;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @Mock
    private OrderBookRepository orderBookRepository;

    @Mock
    private BookCouponRepository bookCouponRepository;

    @Test
    void useCoupon() {
        //given
        Long memberCouponId = 1L;
        Long orderId = 1L;
        Long bookId = 1L;

        Grade grade = Grade.builder()
                .gradeName("골드")
                .build();

        Member member = Member.builder()
                .name("홍길동")
                .grade(grade)
                .build();

        CouponPolicy couponPolicy = CouponPolicy
                .builder()
                .build();

        Coupon coupon = Coupon
                .builder()
                .couponPolicy(couponPolicy)
                .build();

        MemberCoupon memberCoupon = MemberCoupon
                .builder()
                .member(member)
                .coupon(coupon)
                .build();

        OrderBook orderBook = OrderBook.createOrderBook(
                bookId,
                orderId,
                null,
                null,
                5,
                BigDecimal.valueOf(15000)
        );

        when(memberCouponRepository.findById(memberCouponId)).thenReturn(Optional.of(memberCoupon));
        when(orderBookRepository.findById(new OrderBookId(bookId, orderId))).thenReturn(Optional.of(orderBook));



        //when
        MemberCouponResponse memberCouponResponse = memberCouponService.useCoupon(memberCouponId, orderId, bookId);
        //then
        assertThat(memberCouponResponse).isNotNull();
        assertThat(memberCouponResponse.isCouponUsed()).isTrue();
    }

    @Test
    void getUserCoupons() {
    }

    @Test
    void getUserBookCouponsDownloadList() {
    }
}