package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.request.CouponHistoryRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.repository.CouponHistoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.repository.MemberCouponRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookId;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponHistoryServiceImplTest {

    @InjectMocks
    private CouponHistoryServiceImpl couponHistoryService;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @Mock
    private OrderBookRepository orderBookRepository;

    @Mock
    private MemberCouponRepository memberCouponRepository;


    @Test
    void createCouponHistory() {
        //given
        Member member = Member.builder()
                .name("홍길동")
                .phoneNumber("ghdrlfehd@gmail.com")
                .loginId("test2")
                .password("1234")
                .build();

        Coupon coupon = Coupon.builder().build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .member(member)
                .coupon(coupon)
                .expirationDate(LocalDateTime.now().plusDays(7))
                .build();

        CouponHistoryRequest couponHistoryRequest = new CouponHistoryRequest(
                memberCoupon.getId(),
                1L,
                1L
        );

        OrderBook orderBook = OrderBook.createOrderBook(
                1L,
                1L,
                null,
                null,
                5,
                BigDecimal.valueOf(15000)
        );

        when(memberCouponRepository.findById(couponHistoryRequest.memberCouponId())).thenReturn(Optional.of(memberCoupon));
        when(orderBookRepository.findById(new OrderBookId(1L, 1L))).thenReturn(Optional.of(orderBook));

        //when
        couponHistoryService.createCouponHistory(couponHistoryRequest);

        //then
        verify(orderBookRepository, times(1)).findById(new OrderBookId(1L, 1L));
        verify(couponHistoryRepository, times(1)).save(any(CouponHistory.class));

    }
}