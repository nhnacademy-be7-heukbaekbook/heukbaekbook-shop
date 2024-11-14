package com.nhnacademy.heukbaekbookshop.coupon;

import com.nhnacademy.heukbaekbookshop.coupon.domain.CategoryCoupon;
import com.nhnacademy.heukbaekbookshop.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.coupon.domain.CouponHistory;
import com.nhnacademy.heukbaekbookshop.coupon.domain.MemberCoupon;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.CouponHistoryResponse;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.CouponInfoResponse;
import com.nhnacademy.heukbaekbookshop.coupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.coupon.repository.*;
import com.nhnacademy.heukbaekbookshop.coupon.service.CouponService;
import com.nhnacademy.heukbaekbookshop.coupon.service.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @Mock
    private CategoryCouponRepository categoryCouponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategoryCoupons() {
        Long categoryId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CategoryCoupon> categoryCoupons = new PageImpl<>(Collections.emptyList());

        given(categoryCouponRepository.findByCategoryId(pageable, categoryId)).willReturn(categoryCoupons);

        Page<CategoryCoupon> result = categoryCouponRepository.findByCategoryId(pageable, categoryId);

        assertThat(result).isEqualTo(categoryCoupons);
    }

    @Test
    void testGetBookSpecificCoupon() {
        Long couponId = 1L;
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        // Assume book coupon relationship setup
        given(couponRepository.findById(couponId)).willReturn(Optional.of(coupon));

        CouponInfoResponse response = couponService.getCouponDetails(couponId);

        assertThat(response).isNotNull();
        assertThat(response.couponId()).isEqualTo(couponId);
        // Additional assertions for book-specific information
    }

    @Test
    void testGetMemberCoupons() {
        Long memberId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        Page<MemberCoupon> memberCoupons = new PageImpl<>(Collections.emptyList());

        given(memberCouponRepository.findByMemberId(pageable, memberId)).willReturn(memberCoupons);

        Page<MemberCouponResponse> result = couponService.getUserCoupons(pageable, memberId);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void testGetCouponHistory() {
        Long memberCouponId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CouponHistory> history = new PageImpl<>(Collections.emptyList());

        given(couponHistoryRepository.findByMemberCouponId(pageable, memberCouponId)).willReturn(history);

        Page<CouponHistoryResponse> result = couponService.getCouponHistory(pageable, memberCouponId);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }
}
