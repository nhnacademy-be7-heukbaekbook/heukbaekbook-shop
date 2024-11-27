package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.response.MemberCouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.MemberCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberCouponController.class)
class MemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberCouponService memberCouponService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIssueCoupon() throws Exception {
        // Given
        Long customerId = 1L;
        Long couponId = 2L;

        MemberCouponResponse mockResponse = new MemberCouponResponse(
                101L,
                couponId,
                false,
                LocalDateTime.of(2024, 11, 25, 10, 0),
                LocalDateTime.of(2024, 12, 25, 10, 0)
        );

        when(memberCouponService.issueCoupon(anyLong(), anyLong())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(post("/api/members/coupons/{couponId}", couponId)
                        .header(MemberCouponController.X_USER_ID, customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // 응답 상태 코드 확인
                .andExpect(jsonPath("$.memberCouponId").value(101L)) // 회원 쿠폰 ID 확인
                .andExpect(jsonPath("$.couponId").value(couponId)) // 쿠폰 ID 확인
                .andExpect(jsonPath("$.isCouponUsed").value(false)) // 쿠폰 사용 여부 확인
                .andExpect(jsonPath("$.couponIssuedAt").value("2024-11-25T10:00:00")) // 쿠폰 발행 시간 확인
                .andExpect(jsonPath("$.couponExpirationDate").value("2024-12-25T10:00:00")); // 쿠폰 만료 시간 확인

        verify(memberCouponService, times(1)).issueCoupon(anyLong(), anyLong()); // 서비스 호출 검증
    }
}
