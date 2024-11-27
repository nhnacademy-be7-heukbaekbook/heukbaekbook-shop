package com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyRequest;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.service.CouponPolicyService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponPolicyService couponPolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCouponPolicy() throws Exception {
        // Given
        CouponPolicyRequest couponPolicyRequest = new CouponPolicyRequest(
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                new BigDecimal("1000"),
                new BigDecimal("10000")
        );

        CouponPolicyResponse mockResponse = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                new BigDecimal("1000"),
                new BigDecimal("10000")
        );

        when(couponPolicyService.createCouponPolicy(any(CouponPolicyRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/admin/coupons/policy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponPolicyRequest)))
                .andExpect(status().isCreated()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.id").value(1L)) // 정책 ID 확인
                .andExpect(jsonPath("$.discountType").value("PERCENTAGE")) // 할인 타입 확인
                .andExpect(jsonPath("$.discountAmount").value(10)) // 할인 금액 확인
                .andExpect(jsonPath("$.minimumPurchaseAmount").value(1000)) // 최소 구매 금액 확인
                .andExpect(jsonPath("$.maximumPurchaseAmount").value(10000)); // 최대 구매 금액 확인

        verify(couponPolicyService, times(1)).createCouponPolicy(any(CouponPolicyRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testGetCouponPolicy() throws Exception {
        // Given
        Long policyId = 1L;
        CouponPolicyResponse mockResponse = new CouponPolicyResponse(
                policyId,
                DiscountType.FIXED,
                new BigDecimal("5000"),
                new BigDecimal("20000"),
                new BigDecimal("100000")
        );

        when(couponPolicyService.getCouponPolicyById(anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/policy/{policyId}", policyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.id").value(1L)) // 정책 ID 확인
                .andExpect(jsonPath("$.discountType").value("FIXED")) // 할인 타입 확인
                .andExpect(jsonPath("$.discountAmount").value(5000)) // 할인 금액 확인
                .andExpect(jsonPath("$.minimumPurchaseAmount").value(20000)) // 최소 구매 금액 확인
                .andExpect(jsonPath("$.maximumPurchaseAmount").value(100000)); // 최대 구매 금액 확인

        verify(couponPolicyService, times(1)).getCouponPolicyById(anyLong()); // 서비스 호출 검증
    }

    @Test
    void testGetCouponPolicies() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        CouponPolicyResponse policy1 = new CouponPolicyResponse(
                1L,
                DiscountType.FIXED,
                new BigDecimal("5000"),
                new BigDecimal("20000"),
                new BigDecimal("100000")
        );

        CouponPolicyResponse policy2 = new CouponPolicyResponse(
                2L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                new BigDecimal("30000"),
                new BigDecimal("150000")
        );

        Page<CouponPolicyResponse> mockPage = new PageImpl<>(List.of(policy1, policy2), pageable, 2);

        when(couponPolicyService.getAllCouponPolicies(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/policy")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.content[0].id").value(1L)) // 첫 번째 정책 ID 확인
                .andExpect(jsonPath("$.content[0].discountType").value("FIXED")) // 첫 번째 할인 타입 확인
                .andExpect(jsonPath("$.content[0].discountAmount").value(5000)) // 첫 번째 할인 금액 확인
                .andExpect(jsonPath("$.content[0].minimumPurchaseAmount").value(20000)) // 첫 번째 최소 구매 금액 확인
                .andExpect(jsonPath("$.content[0].maximumPurchaseAmount").value(100000)) // 첫 번째 최대 구매 금액 확인
                .andExpect(jsonPath("$.content[1].id").value(2L)) // 두 번째 정책 ID 확인
                .andExpect(jsonPath("$.content[1].discountType").value("PERCENTAGE")) // 두 번째 할인 타입 확인
                .andExpect(jsonPath("$.content[1].discountAmount").value(10)) // 두 번째 할인 금액 확인
                .andExpect(jsonPath("$.content[1].minimumPurchaseAmount").value(30000)) // 두 번째 최소 구매 금액 확인
                .andExpect(jsonPath("$.content[1].maximumPurchaseAmount").value(150000)); // 두 번째 최대 구매 금액 확인

        verify(couponPolicyService, times(1)).getAllCouponPolicies(any(Pageable.class)); // 서비스 호출 검증
    }

    @Test
    void testGetCouponPolicyList() throws Exception {
        // Given
        CouponPolicyResponse policy1 = new CouponPolicyResponse(
                1L,
                DiscountType.FIXED,
                new BigDecimal("5000"),
                new BigDecimal("20000"),
                new BigDecimal("100000")
        );

        CouponPolicyResponse policy2 = new CouponPolicyResponse(
                2L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10"),
                new BigDecimal("30000"),
                new BigDecimal("150000")
        );

        List<CouponPolicyResponse> mockResponse = List.of(policy1, policy2);

        when(couponPolicyService.getAllCouponPolicyList()).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/admin/coupons/policy/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$[0].id").value(1L)) // 첫 번째 정책 ID 확인
                .andExpect(jsonPath("$[0].discountType").value("FIXED")) // 첫 번째 할인 타입 확인
                .andExpect(jsonPath("$[0].discountAmount").value(5000)) // 첫 번째 할인 금액 확인
                .andExpect(jsonPath("$[0].minimumPurchaseAmount").value(20000)) // 첫 번째 최소 구매 금액 확인
                .andExpect(jsonPath("$[0].maximumPurchaseAmount").value(100000)) // 첫 번째 최대 구매 금액 확인
                .andExpect(jsonPath("$[1].id").value(2L)) // 두 번째 정책 ID 확인
                .andExpect(jsonPath("$[1].discountType").value("PERCENTAGE")) // 두 번째 할인 타입 확인
                .andExpect(jsonPath("$[1].discountAmount").value(10)) // 두 번째 할인 금액 확인
                .andExpect(jsonPath("$[1].minimumPurchaseAmount").value(30000)) // 두 번째 최소 구매 금액 확인
                .andExpect(jsonPath("$[1].maximumPurchaseAmount").value(150000)); // 두 번째 최대 구매 금액 확인

        verify(couponPolicyService, times(1)).getAllCouponPolicyList(); // 서비스 호출 검증
    }

    @Test
    void testUpdateCouponPolicy() throws Exception {
        // Given
        Long policyId = 1L;
        CouponPolicyRequest couponPolicyRequest = new CouponPolicyRequest(
                DiscountType.PERCENTAGE,
                new BigDecimal("15"),
                new BigDecimal("20000"),
                new BigDecimal("100000")
        );

        CouponPolicyResponse mockResponse = new CouponPolicyResponse(
                policyId,
                DiscountType.PERCENTAGE,
                new BigDecimal("15"),
                new BigDecimal("20000"),
                new BigDecimal("100000")
        );

        when(couponPolicyService.updateCouponPolicy(anyLong(), any(CouponPolicyRequest.class))).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(put("/api/admin/coupons/policy/{policyId}", policyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponPolicyRequest)))
                .andExpect(status().isOk()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.id").value(policyId)) // 정책 ID 확인
                .andExpect(jsonPath("$.discountType").value("PERCENTAGE")) // 할인 타입 확인
                .andExpect(jsonPath("$.discountAmount").value(15)) // 할인 금액 확인
                .andExpect(jsonPath("$.minimumPurchaseAmount").value(20000)) // 최소 구매 금액 확인
                .andExpect(jsonPath("$.maximumPurchaseAmount").value(100000)); // 최대 구매 금액 확인

        verify(couponPolicyService, times(1)).updateCouponPolicy(anyLong(), any(CouponPolicyRequest.class)); // 서비스 호출 검증
    }

    @Test
    void testDeleteCouponPolicy() throws Exception {
        // Given
        Long policyId = 1L;

        // Mocking the service method
        doNothing().when(couponPolicyService).deleteCouponPolicy(anyLong());

        // When & Then
        mockMvc.perform(delete("/api/admin/coupons/policy/{policyId}", policyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // 응답 상태 코드 확인

        verify(couponPolicyService, times(1)).deleteCouponPolicy(anyLong()); // 서비스 호출 검증
    }

}
