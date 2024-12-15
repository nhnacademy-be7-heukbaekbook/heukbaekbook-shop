package com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.impl;


import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.category.domain.Category;
import com.nhnacademy.heukbaekbookshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.heukbaekbookshop.category.repository.CategoryRepository;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.BookCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.CategoryCoupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.Coupon;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.CouponPolicy;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.repository.CouponPolicyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.QCoupon.coupon;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Sql("coupon-test.sql")
class CouponRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        // 데이터 세팅
        CouponPolicy policy = CouponPolicy.
                builder().
                discountType(DiscountType.FIXED).
                discountAmount(BigDecimal.valueOf(1000)).
                minimumPurchaseAmount(BigDecimal.ZERO).
                maximumPurchaseAmount(BigDecimal.valueOf(10000)).
                build();
        couponPolicyRepository.save(policy);

        Coupon normalCoupon = Coupon.
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

        couponRepository.save(normalCoupon);

        Book book = bookRepository.findById(1L)
                .orElseThrow(() -> new BookNotFoundException(1L));

        BookCoupon bookCoupon = new BookCoupon(policy, 100, 100, LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Book Coupon", "description", book);
        couponRepository.save(bookCoupon);

        Category category = categoryRepository.findById(1L)
                .orElseThrow(() -> new CategoryNotFoundException("category not found"));

        CategoryCoupon categoryCoupon = new CategoryCoupon(policy, 100, 100, LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Category Coupon", "description", category);
        couponRepository.save(categoryCoupon);
    }

    @Test
    void findAllNormalCoupons_ShouldReturnCoupons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Coupon> result = couponRepository.findAllNormalCoupons(pageable);

        // Then
        assertThat(result.getContent())
                .isNotNull()
                .hasSize(1)
                .extracting(Coupon::getCouponName)
                .contains("Normal Coupon");
    }

    @Test
    void findAllBookCoupons_ShouldReturnBookCoupons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<BookCouponResponse> result = couponRepository.findAllBookCoupons(pageable);

        // Then
        assertThat(result.getContent())
                .isNotNull()
                .hasSize(1)
                .extracting(BookCouponResponse::couponName)
                .contains("Book Coupon");
    }

    @Test
    void findAllCategoryCoupons_ShouldReturnCategoryCoupons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<CategoryCouponResponse> result = couponRepository.findAllCategoryCoupons(pageable);

        // Then
        assertThat(result.getContent())
                .isNotNull()
                .hasSize(1)
                .extracting(CategoryCouponResponse::couponName)
                .contains("Category Coupon");
    }

    @Test
    void findDownloadableCouponsByBookId_ShouldReturnCoupons() {
        // Given
        Long bookId = 1L;

        // When
        List<Coupon> result = couponRepository.findDownloadableCouponsByBookId(bookId);

        // Then
        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .extracting(Coupon::getCouponName)
                .contains("Book Coupon");
    }

    @Test
    void findAllByCouponStatus_ShouldReturnCorrectCoupons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CouponStatus status = CouponStatus.ABLE;

        // When
        Page<Coupon> result = couponRepository.findAllByCouponStatus(status, pageable);

        // Then
        assertThat(result.getContent())
                .isNotNull()
                .hasSize(3)
                .extracting(Coupon::getCouponName)
                .containsExactly("Category Coupon", "Book Coupon", "Normal Coupon");

        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void findAllByDiscountType_ShouldReturnCorrectCoupons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        DiscountType discountType = DiscountType.FIXED;

        // When
        Page<Coupon> result = couponRepository.findAllByDiscountType(discountType, pageable);

        // Then
        assertThat(result.getContent())
                .isNotNull()
                .hasSize(3)
                .extracting(Coupon::getCouponName)
                .containsExactly("Category Coupon", "Book Coupon", "Normal Coupon");

        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void findAvailableCouponById() {
        //given
        CouponType couponType = CouponType.GENERAL;

        //when
        Optional<Long> result = couponRepository.findAvailableCouponIdByCouponType(couponType);

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(7L);
    }
}