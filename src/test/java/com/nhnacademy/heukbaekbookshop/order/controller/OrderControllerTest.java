//package com.nhnacademy.heukbaekbookshop.order.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.heukbaekbookshop.order.controller.OrderController;
//import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderBookRequest;
//import com.nhnacademy.heukbaekbookshop.order.dto.request.OrderCreateRequest;
//import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderBookResponse;
//import com.nhnacademy.heukbaekbookshop.order.dto.response.OrderDetailResponse;
//import com.nhnacademy.heukbaekbookshop.order.service.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrderController.class)
//class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private OrderService orderService;
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
//    void testCreateOrder() throws Exception {
//        // Mock OrderCreateRequest 생성
//        OrderBookRequest bookRequest = new OrderBookRequest(1L, 2, "20000", false, null);
//        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
//                1L,
//                "50000",
//                "test@example.com",
//                "홍길동",
//                "010-1234-5678",
//                "TOSS12345",
//                "홍길동",
//                "12345",
//                "서울특별시 강남구 테헤란로",
//                "101호",
//                "010-8765-4321",
//                "5000",
//                List.of(bookRequest)
//        );
//
//        // Mock 서비스 동작 설정
//        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(1L);
//
//        // 테스트 수행
//        mockMvc.perform(post("/api/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("1"));
//
//        // Verify 서비스 호출 확인
//        verify(orderService, times(1)).createOrder(any(OrderCreateRequest.class));
//    }
//
//    @Test
//    void testGetOrderDetailResponse() throws Exception {
//        // Mock OrderDetailResponse 생성
//        OrderBookResponse bookResponse = new OrderBookResponse(
//                "https://example.com/book-thumbnail.jpg",
//                "Java Programming",
//                "10000",
//                1,
//                "9000",
//                new BigDecimal("10"),
//                "9000"
//        );
//
//        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
//                "홍길동",
//                "5000",
//                "45000",
//                "신용카드",
//                "홍길동",
//                12345L,
//                "서울특별시 강남구 테헤란로",
//                "101호",
//                "40000",
//                "10000",
//                "50000",
//                List.of(bookResponse)
//        );
//
//        // Mock 서비스 동작 설정
//        when(orderService.getOrderDetailResponse(eq("1"))).thenReturn(orderDetailResponse);
//
//        // 테스트 수행
//        mockMvc.perform(get("/api/orders/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.customerName").value("홍길동"))
//                .andExpect(jsonPath("$.deliveryFee").value("5000"))
//                .andExpect(jsonPath("$.paymentPrice").value("45000"))
//                .andExpect(jsonPath("$.recipient").value("홍길동"))
//                .andExpect(jsonPath("$.postalCode").value(12345L))
//                .andExpect(jsonPath("$.roadNameAddress").value("서울특별시 강남구 테헤란로"))
//                .andExpect(jsonPath("$.books[0].title").value("Java Programming"))
//                .andExpect(jsonPath("$.books[0].quantity").value(1));
//
//        // Verify 서비스 호출 확인
//        verify(orderService, times(1)).getOrderDetailResponse(eq("1"));
//    }
//}
