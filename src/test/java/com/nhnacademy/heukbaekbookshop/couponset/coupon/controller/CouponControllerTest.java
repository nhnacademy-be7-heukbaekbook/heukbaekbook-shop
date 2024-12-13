package com.nhnacademy.heukbaekbookshop.couponset.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType.FIXED;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCoupon() throws Exception {
        // Given
        Long couponId = 1L;
        CouponPolicyResponse mockPolicyResponse = new CouponPolicyResponse(1L, FIXED, BigDecimal.valueOf(15L), BigDecimal.valueOf(5000L),BigDecimal.valueOf(10000L));
        CouponResponse mockResponse = new CouponResponse(
                couponId,
                mockPolicyResponse,
                CouponStatus.ABLE,
                100,
                30,
                LocalDateTime.of(2024, 11, 1, 0, 0),
                LocalDateTime.of(2024, 12, 1, 0, 0),
                "Sample Coupon",
                "This is a sample coupon description.",
                LocalDateTime.of(2024, 10, 1, 12, 0),
                CouponType.GENERAL
        );

        when(couponService.getCoupon(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/{couponId}", couponId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.couponId").value(1L));

        verify(couponService, times(1)).getCoupon(anyLong()); // 서비스 호출 검증
    }

    @Test
    void testGetAllNormalCoupons() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CouponPolicyResponse mockPolicyResponse1 = new CouponPolicyResponse(1L, FIXED, BigDecimal.valueOf(15L), BigDecimal.valueOf(5000L),BigDecimal.valueOf(10000L));
        CouponPolicyResponse mockPolicyResponse2 = new CouponPolicyResponse(2L, FIXED, BigDecimal.valueOf(15L), BigDecimal.valueOf(5000L),BigDecimal.valueOf(10000L));

        CouponResponse coupon1 = new CouponResponse(
                1L,
                mockPolicyResponse1,
                CouponStatus.ABLE,
                100,
                30,
                LocalDateTime.of(2024, 11, 1, 0, 0),
                LocalDateTime.of(2024, 12, 1, 0, 0),
                "Coupon 1",
                "Description 1",
                LocalDateTime.of(2024, 10, 1, 12, 0),
                CouponType.GENERAL
        );

        CouponResponse coupon2 = new CouponResponse(
                2L,
                mockPolicyResponse2,
                CouponStatus.ABLE,
                50,
                15,
                LocalDateTime.of(2024, 11, 5, 0, 0),
                LocalDateTime.of(2024, 11, 20, 0, 0),
                "Coupon 2",
                "Description 2",
                LocalDateTime.of(2024, 10, 2, 12, 0),
                CouponType.GENERAL
        );

        Page<CouponResponse> mockPage = new PageImpl<>(List.of(coupon1, coupon2), pageable, 2);

        when(couponService.getAllNormalCoupons(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].couponId").value(1L));

        verify(couponService, times(1)).getAllNormalCoupons(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetAllBookCoupons() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        BookCouponResponse bookCoupon1 = new BookCouponResponse(
                1L,
                "Book Coupon 1",
                "Description for Book Coupon 1",
                LocalDateTime.of(2024, 11, 1, 0, 0),
                CouponStatus.ABLE,
                100,
                30,
                LocalDateTime.of(2024, 11, 1, 0, 0),
                LocalDateTime.of(2024, 12, 1, 0, 0),
                CouponType.GENERAL,
                101L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                new BigDecimal("500"),
                new BigDecimal("10000"),
                1L,
                "Book Title 1"
        );

        BookCouponResponse bookCoupon2 = new BookCouponResponse(
                2L,
                "Book Coupon 2",
                "Description for Book Coupon 2",
                LocalDateTime.of(2024, 11, 5, 0, 0),
                CouponStatus.ABLE,
                50,
                15,
                LocalDateTime.of(2024, 11, 5, 0, 0),
                LocalDateTime.of(2024, 11, 20, 0, 0),
                CouponType.GENERAL,
                102L,
                DiscountType.FIXED,
                new BigDecimal("5000"),
                new BigDecimal("1000"),
                new BigDecimal("20000"),
                2L,
                "Book Title 2"
        );

        Page<BookCouponResponse> mockPage = new PageImpl<>(List.of(bookCoupon1, bookCoupon2), pageable, 2);

        when(couponService.getAllBookCoupons(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/book-coupon")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].bookCouponId").value(1L)) // 첫 번째 북 쿠폰 ID 확인
                .andExpect(jsonPath("$.content[0].couponName").value("Book Coupon 1")) // 첫 번째 북 쿠폰 이름 확인
                .andExpect(jsonPath("$.content[0].title").value("Book Title 1")) // 첫 번째 북 쿠폰 책 제목 확인
                .andExpect(jsonPath("$.content[1].bookCouponId").value(2L)) // 두 번째 북 쿠폰 ID 확인
                .andExpect(jsonPath("$.content[1].couponName").value("Book Coupon 2")) // 두 번째 북 쿠폰 이름 확인
                .andExpect(jsonPath("$.content[1].title").value("Book Title 2")); // 두 번째 북 쿠폰 책 제목 확인

        verify(couponService, times(1)).getAllBookCoupons(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetAllCategoryCoupons() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CategoryCouponResponse categoryCoupon1 = new CategoryCouponResponse(
                1L,
                "Category Coupon 1",
                "Description for Category Coupon 1",
                LocalDateTime.of(2024, 11, 1, 0, 0),
                CouponStatus.ABLE,
                100,
                30,
                LocalDateTime.of(2024, 11, 1, 0, 0),
                LocalDateTime.of(2024, 12, 1, 0, 0),
                CouponType.GENERAL,
                101L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                new BigDecimal("500"),
                new BigDecimal("10000"),
                1L,
                "Category Name 1"
        );

        CategoryCouponResponse categoryCoupon2 = new CategoryCouponResponse(
                2L,
                "Category Coupon 2",
                "Description for Category Coupon 2",
                LocalDateTime.of(2024, 11, 5, 0, 0),
                CouponStatus.ABLE,
                50,
                15,
                LocalDateTime.of(2024, 11, 5, 0, 0),
                LocalDateTime.of(2024, 11, 20, 0, 0),
                CouponType.GENERAL,
                102L,
                DiscountType.FIXED,
                new BigDecimal("5000"),
                new BigDecimal("1000"),
                new BigDecimal("20000"),
                2L,
                "Category Name 2"
        );

        Page<CategoryCouponResponse> mockPage = new PageImpl<>(List.of(categoryCoupon1, categoryCoupon2), pageable, 2);

        when(couponService.getAllCategoryCoupons(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/category-coupon")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].categoryCouponId").value(1L)) // 첫 번째 카테고리 쿠폰 ID 확인
                .andExpect(jsonPath("$.content[0].couponName").value("Category Coupon 1")) // 첫 번째 카테고리 쿠폰 이름 확인
                .andExpect(jsonPath("$.content[0].name").value("Category Name 1")) // 첫 번째 카테고리 이름 확인
                .andExpect(jsonPath("$.content[1].categoryCouponId").value(2L)) // 두 번째 카테고리 쿠폰 ID 확인
                .andExpect(jsonPath("$.content[1].couponName").value("Category Coupon 2")) // 두 번째 카테고리 쿠폰 이름 확인
                .andExpect(jsonPath("$.content[1].name").value("Category Name 2")); // 두 번째 카테고리 이름 확인

        verify(couponService, times(1)).getAllCategoryCoupons(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetCouponsByStatus() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CouponStatus status = CouponStatus.ABLE;

        CouponPolicyResponse mockPolicyResponse1 = new CouponPolicyResponse(1L, FIXED, BigDecimal.valueOf(15L), BigDecimal.valueOf(5000L),BigDecimal.valueOf(10000L));
        CouponPolicyResponse mockPolicyResponse2 = new CouponPolicyResponse(2L, FIXED, BigDecimal.valueOf(15L), BigDecimal.valueOf(5000L),BigDecimal.valueOf(10000L));

        CouponResponse coupon1 = new CouponResponse(
                1L,
                mockPolicyResponse1,
                status,
                100,
                30,
                LocalDateTime.of(2024, 11, 1, 0, 0),
                LocalDateTime.of(2024, 12, 1, 0, 0),
                "Coupon 1",
                "Description 1",
                LocalDateTime.of(2024, 10, 1, 12, 0),
                CouponType.GENERAL
        );

        CouponResponse coupon2 = new CouponResponse(
                2L,
                mockPolicyResponse2,
                status,
                50,
                15,
                LocalDateTime.of(2024, 11, 5, 0, 0),
                LocalDateTime.of(2024, 11, 20, 0, 0),
                "Coupon 2",
                "Description 2",
                LocalDateTime.of(2024, 10, 2, 12, 0),
                CouponType.GENERAL
        );

        Page<CouponResponse> mockPage = new PageImpl<>(List.of(coupon1, coupon2), pageable, 2);

        when(couponService.getCouponsByStatus(any(CouponStatus.class), any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/status/{status}", status)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].couponId").value(1L)) // 첫 번째 쿠폰 ID 확인
                .andExpect(jsonPath("$.content[0].couponName").value("Coupon 1")) // 첫 번째 쿠폰 이름 확인
                .andExpect(jsonPath("$.content[0].couponPolicyResponse.id").value(1L)) // 첫 번째 정책 이름 확인
                .andExpect(jsonPath("$.content[1].couponId").value(2L)) // 두 번째 쿠폰 ID 확인
                .andExpect(jsonPath("$.content[1].couponName").value("Coupon 2")) // 두 번째 쿠폰 이름 확인
                .andExpect(jsonPath("$.content[1].couponPolicyResponse.id").value(2L)); // 두 번째 정책 이름 확인

        verify(couponService, times(1)).getCouponsByStatus(any(CouponStatus.class), any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testCreateCoupon() throws Exception {
        // Given
        CouponRequest couponRequest = new CouponRequest(
                1l,
                10,
                50,
                LocalDateTime.of(2024, 11, 25, 0, 0),
                LocalDateTime.of(2024, 12, 25, 0, 0),
                "abc",
                "abc",
                CouponType.BOOK,
                1L,
                null
        );

        CouponPolicyResponse mockPolicyResponse = new CouponPolicyResponse(
                1L,
                DiscountType.FIXED,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                new BigDecimal("20000")
        );

        CouponResponse mockResponse = new CouponResponse(
                1L,
                mockPolicyResponse,
                CouponStatus.ABLE,
                50,
                10,
                LocalDateTime.of(2024, 11, 25, 0, 0),
                LocalDateTime.of(2024, 12, 25, 0, 0),
                "Test Coupon",
                "Test Coupon Description",
                LocalDateTime.of(2024, 11, 1, 12, 0),
                CouponType.GENERAL
        );

        when(couponService.createCoupon(any(CouponRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/admin/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponRequest)))
                .andExpect(status().isCreated()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.couponId").value(1L)) // 쿠폰 ID 확인
                .andExpect(jsonPath("$.couponName").value("Test Coupon")) // 쿠폰 이름 확인
                .andExpect(jsonPath("$.couponPolicyResponse.discountType").value("FIXED")) // 할인 타입 확인
                .andExpect(jsonPath("$.couponPolicyResponse.discountAmount").value(1000)); // 할인 금액 확인

        verify(couponService, times(1)).createCoupon(any(CouponRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testUpdateCoupon() throws Exception {
        // Given
        Long couponId = 1L;
        CouponRequest couponRequest = new CouponRequest(
                1L,
                50,
                10,
                LocalDateTime.of(2024, 11, 25, 0, 0),
                LocalDateTime.of(2024, 12, 25, 0, 0),
                "Updated Coupon",
                "Updated Description",
                null,
                null,
                null
        );

        CouponPolicyResponse mockPolicyResponse = new CouponPolicyResponse(
                1L,
                DiscountType.FIXED,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                new BigDecimal("20000")
        );

        CouponResponse mockResponse = new CouponResponse(
                couponId,
                mockPolicyResponse,
                CouponStatus.ABLE,
                50,
                10,
                LocalDateTime.of(2024, 11, 25, 0, 0),
                LocalDateTime.of(2024, 12, 25, 0, 0),
                "Updated Coupon",
                "Updated Description",
                LocalDateTime.of(2024, 11, 1, 12, 0),
                CouponType.GENERAL
        );

        when(couponService.updateCoupon(anyLong(), any(CouponRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/coupons/{couponId}", couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponRequest)))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.couponId").value(1L)) // 쿠폰 ID 확인
                .andExpect(jsonPath("$.couponName").value("Updated Coupon")) // 업데이트된 쿠폰 이름 확인
                .andExpect(jsonPath("$.couponPolicyResponse.discountType").value("FIXED")) // 할인 타입 확인
                .andExpect(jsonPath("$.couponPolicyResponse.discountAmount").value(1000)); // 할인 금액 확인

        verify(couponService, times(1)).updateCoupon(anyLong(), any(CouponRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testChangeIssuedCoupon() throws Exception {
        // Given
        Long couponId = 1L;

        // Mocking the service method
        doNothing().when(couponService).changeCouponStatus(anyLong(), eq(CouponStatus.ABLE));

        // When & Then
        mockMvc.perform(delete("/api/admin/coupons/{couponId}", couponId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // 응답 상태 코드 확인

        verify(couponService, times(1)).changeCouponStatus(couponId, CouponStatus.DISABLE); // 서비스 호출 검증
    }

}
