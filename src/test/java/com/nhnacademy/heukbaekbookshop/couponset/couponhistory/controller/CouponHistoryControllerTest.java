//package com.nhnacademy.heukbaekbookshop.couponset.couponhistory.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.request.CouponHistoryRequest;
//import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.dto.response.CouponHistoryResponse;
//import com.nhnacademy.heukbaekbookshop.couponset.couponhistory.service.CouponHistoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CouponHistoryController.class)
//class CouponHistoryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CouponHistoryService couponHistoryService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetCouponHistoriesByUser() throws Exception {
//        // Given
//        Long memberId = 1L;
//        Pageable pageable = PageRequest.of(0, 10);
//
//        CouponHistoryResponse history1 = new CouponHistoryResponse(
//                1L,
//                101L,
//                memberId,
//                201L,
//                LocalDateTime.of(2024, 11, 1, 12, 0),
//                301L,
//                401L
//        );
//
//        CouponHistoryResponse history2 = new CouponHistoryResponse(
//                2L,
//                102L,
//                memberId,
//                202L,
//                LocalDateTime.of(2024, 11, 2, 14, 0),
//                302L,
//                402L
//        );
//
//        Page<CouponHistoryResponse> mockPage = new PageImpl<>(List.of(history1, history2), pageable, 2);
//
//        when(couponHistoryService.getCouponHistoryByCustomerId(anyLong(), any(Pageable.class))).thenReturn(mockPage);
//
//        // When & Then
//        mockMvc.perform(get("/api/coupons/histories/members/{memberId}", memberId)
//                        .param("page", "0")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) // 응답 상태 코드 확인
//                .andExpect(jsonPath("$.content[0].couponHistoryId").value(1L)) // 첫 번째 내역 ID 확인
//                .andExpect(jsonPath("$.content[0].memberId").value(memberId)) // 첫 번째 내역 사용자 ID 확인
//                .andExpect(jsonPath("$.content[0].couponId").value(201L)) // 첫 번째 쿠폰 ID 확인
//                .andExpect(jsonPath("$.content[0].usedAt").value("2024-11-01T12:00:00")) // 첫 번째 사용 시간 확인
//                .andExpect(jsonPath("$.content[1].couponHistoryId").value(2L)) // 두 번째 내역 ID 확인
//                .andExpect(jsonPath("$.content[1].memberId").value(memberId)) // 두 번째 내역 사용자 ID 확인
//                .andExpect(jsonPath("$.content[1].couponId").value(202L)) // 두 번째 쿠폰 ID 확인
//                .andExpect(jsonPath("$.content[1].usedAt").value("2024-11-02T14:00:00")); // 두 번째 사용 시간 확인
//
//        verify(couponHistoryService, times(1)).getCouponHistoryByCustomerId(anyLong(), any(Pageable.class)); // 서비스 호출 검증
//    }
//
//    @Test
//    void testCreateCouponHistory() throws Exception {
//        // Given
//        CouponHistoryRequest couponHistoryRequest = new CouponHistoryRequest(
//                1L,
//                2L,
//                3L
//        );
//
//        doNothing().when(couponHistoryService).createCouponHistory(any(CouponHistoryRequest.class));
//
//        // When & Then
//        mockMvc.perform(post("/api/coupons/histories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(couponHistoryRequest)))
//                .andExpect(status().isCreated()); // 응답 상태 코드 확인
//
//        verify(couponHistoryService, times(1)).createCouponHistory(any(CouponHistoryRequest.class)); // 서비스 호출 검증
//    }
//
//}
