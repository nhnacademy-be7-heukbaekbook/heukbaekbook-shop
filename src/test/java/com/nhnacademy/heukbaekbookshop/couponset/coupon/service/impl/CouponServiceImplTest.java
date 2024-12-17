package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @InjectMocks
    private CouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CouponRedisService couponRedisService;

    @Test
    void createCoupon_ShouldCreateBookCoupon() {
        // Given
        CouponPolicy policy = CouponPolicy.builder()
                .discountType(DiscountType.FIXED)
                .discountAmount(BigDecimal.valueOf(5000))
                .minimumPurchaseAmount(BigDecimal.valueOf(10000))
                .maximumPurchaseAmount(BigDecimal.valueOf(50000))
                .build();

        CouponRequest request = new CouponRequest(
                1L,
                100,
                7,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Book Coupon",
                "description",
                CouponType.BOOK,
                1L,
                null
        );

        when(couponPolicyRepository.findById(request.policyId())).thenReturn(Optional.of(policy));

        Book book = new Book();
        when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(book));

        Coupon coupon = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Book Coupon").
                couponDescription("description").
                couponType(CouponType.BOOK).
                build();
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // When
        CouponResponse response = couponService.createCoupon(request);

        // Then
        assertThat(response).isNotNull();
        verify(couponPolicyRepository, times(1)).findById(request.policyId());
        verify(bookRepository, times(1)).findById(request.bookId());
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void createCoupon_ShouldThrowCouponPolicyNotFoundException() {
        // Given
        CouponRequest request = new CouponRequest(
                1L,
                100,
                7,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Normal Coupon",
                "description",
                CouponType.GENERAL,
                1L,
                1L
        );

        when(couponPolicyRepository.findById(request.policyId())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> couponService.createCoupon(request))
                .isInstanceOf(CouponPolicyNotFoundException.class);
        verify(couponPolicyRepository, times(1)).findById(request.policyId());
    }

    @Test
    void getCoupon_ShouldReturnCouponResponse() {
        // Given
        Long couponId = 1L;

        CouponPolicy policy = new CouponPolicy();

        Coupon coupon = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        // When
        CouponResponse response = couponService.getCoupon(couponId);

        // Then
        assertThat(response).isNotNull();
        verify(couponRepository, times(1)).findById(couponId);
    }

    @Test
    void getDownloadableCoupons_ShouldReturnCoupons() {
        // Given
        Long bookId = 1L;
        CouponPolicy policy = new CouponPolicy();

        Coupon coupon1 = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();

        Coupon coupon2 = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();

        List<Coupon> coupons = List.of(coupon1, coupon2);
        when(couponRepository.findDownloadableCouponsByBookId(bookId)).thenReturn(coupons);

        // When
        List<CouponResponse> responses = couponService.getDownloadableCoupons(bookId);

        // Then
        assertThat(responses.size()).isEqualTo(2);
        verify(couponRepository, times(1)).findDownloadableCouponsByBookId(bookId);
    }

    @Test
    void getAllNormalCoupons_ShouldReturnPagedCoupons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CouponPolicy policy = new CouponPolicy();

        Coupon coupon1 = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();

        Coupon coupon2 = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();

        Page<Coupon> coupons = new PageImpl<>(List.of(coupon1, coupon2));
        when(couponRepository.findAllNormalCoupons(pageable)).thenReturn(coupons);

        // When
        Page<CouponResponse> responses = couponService.getAllNormalCoupons(pageable);

        // Then
        assertThat(responses.getContent().size()).isEqualTo(2);
        verify(couponRepository, times(1)).findAllNormalCoupons(pageable);
    }

    @Test
    void updateCoupon_ShouldUpdateCoupon() {
        // Given
        Long couponId = 1L;
        CouponPolicy policy = new CouponPolicy();

        CouponRequest request = new CouponRequest(
                1L,
                100,
                7,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                "Normal Coupon",
                "description",
                CouponType.GENERAL,
                null,
                null
        );

        Coupon coupon = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        when(couponPolicyRepository.findById(request.policyId())).thenReturn(Optional.of(policy));

        // When
        CouponResponse response = couponService.updateCoupon(couponId, request);

        // Then
        assertThat(response).isNotNull();
        verify(couponRepository, times(1)).findById(couponId);
        verify(couponPolicyRepository, times(1)).findById(request.policyId());
    }

    @Test
    void changeCouponStatus_ShouldUpdateStatus() {
        // Given
        Long couponId = 1L;
        CouponPolicy policy = new CouponPolicy();

        Coupon coupon = Coupon.
                builder().
                couponPolicy(policy).
                couponQuantity(100).
                availableDuration(100).
                couponTimeStart(LocalDateTime.now()).
                couponTimeEnd(LocalDateTime.now().plusDays(7)).
                couponName("Normal Coupon").
                couponDescription("description").
                couponType(CouponType.GENERAL).
                build();
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        // When
        couponService.changeCouponStatus(couponId, CouponStatus.DISABLE);

        // Then
        verify(couponRepository, times(1)).findById(couponId);
        verify(couponRedisService, times(1)).deleteCouponFromRedis(couponId);
    }
}