//package com.nhnacademy.heukbaekbookshop.order.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.heukbaekbookshop.order.controller.PaymentController;
//import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
//import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
//import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalFailResponse;
//import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
//import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentCancelResponse;
//import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentDetailResponse;
//import com.nhnacademy.heukbaekbookshop.order.service.PaymentService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(PaymentController.class)
//class PaymentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PaymentService paymentService;
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
//    void testConfirmPaymentSuccess() throws Exception {
//        // Mock PaymentApprovalRequest 생성
//        PaymentApprovalRequest request = new PaymentApprovalRequest(
//                "paymentKey123",
//                "order123",
//                50000L,
//                "CARD"
//        );
//
//        // Mock PaymentApprovalResponse 생성
//        PaymentApprovalResponse response = new PaymentApprovalResponse(
//                "2024-11-01T10:00:00",
//                "CONFIRMED",
//                "홍길동",
//                "010-1234-5678",
//                "test@example.com",
//                "CARD",
//                "2024-11-01T10:05:00",
//                "2024-11-01T10:10:00",
//                50000,
//                "결제가 성공적으로 완료되었습니다."
//        );
//
//        // Mock 서비스 동작 설정
//        when(paymentService.approvePayment(any(PaymentApprovalRequest.class))).thenReturn(response);
//
//        // 테스트 수행
//        mockMvc.perform(post("/api/payments/confirm")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.orderCreatedAt").value("2024-11-01T10:00:00"))
//                .andExpect(jsonPath("$.orderStatus").value("CONFIRMED"))
//                .andExpect(jsonPath("$.orderCustomerName").value("홍길동"))
//                .andExpect(jsonPath("$.orderCustomerPhoneNumber").value("010-1234-5678"))
//                .andExpect(jsonPath("$.paymentPrice").value(50000))
//                .andExpect(jsonPath("$.message").value("결제가 성공적으로 완료되었습니다."));
//
//        // Verify 서비스 호출 확인
//        verify(paymentService, times(1)).approvePayment(any(PaymentApprovalRequest.class));
//    }
//
//    @Test
//    void testConfirmPaymentFailure() throws Exception {
//        // Mock PaymentApprovalRequest 생성
//        PaymentApprovalRequest request = new PaymentApprovalRequest(
//                "invalidPaymentKey",
//                "order123",
//                50000L,
//                "CARD"
//        );
//
//        // Mock 서비스 동작 설정
//        doThrow(new RuntimeException("결제 실패")).when(paymentService).approvePayment(any(PaymentApprovalRequest.class));
//
//        // 테스트 수행
//        mockMvc.perform(post("/api/payments/confirm")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("결제에 실패하였습니다."));
//
//        // Verify 서비스 호출 확인
//        verify(paymentService, times(1)).approvePayment(any(PaymentApprovalRequest.class));
//    }
//
//    @Test
//    void testCancelPaymentSuccess() throws Exception {
//        // Mock PaymentCancelRequest 생성
//        PaymentCancelRequest request = new PaymentCancelRequest("상품 품절", "CARD");
//
//        // Mock PaymentCancelResponse 생성
//        PaymentCancelResponse response = new PaymentCancelResponse("결제가 성공적으로 취소되었습니다.");
//
//        // Mock 서비스 동작 설정
//        when(paymentService.cancelPayment(eq("paymentKey123"), any(PaymentCancelRequest.class))).thenReturn(response);
//
//        // 테스트 수행
//        mockMvc.perform(post("/api/payments/paymentKey123/cancel")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("결제가 성공적으로 취소되었습니다."));
//
//        // Verify 서비스 호출 확인
//        verify(paymentService, times(1)).cancelPayment(eq("paymentKey123"), any(PaymentCancelRequest.class));
//    }
//
//    @Test
//    void testCancelPaymentFailure() throws Exception {
//        // Mock PaymentCancelRequest 생성
//        PaymentCancelRequest request = new PaymentCancelRequest("상품 품절", "CARD");
//
//        // Mock 서비스 동작 설정
//        doThrow(new RuntimeException("결제 취소 실패")).when(paymentService).cancelPayment(eq("paymentKey123"), any(PaymentCancelRequest.class));
//
//        // 테스트 수행
//        mockMvc.perform(post("/api/payments/paymentKey123/cancel")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("결제 취소 요청이 실패하였습니다."));
//
//        // Verify 서비스 호출 확인
//        verify(paymentService, times(1)).cancelPayment(eq("paymentKey123"), any(PaymentCancelRequest.class));
//    }
//
//    @Test
//    void testGetPaymentSuccess() throws Exception {
//        // Mock PaymentDetailResponse 생성
//        PaymentDetailResponse mockResponse = new PaymentDetailResponse(
//                "payment123",
//                "CARD",
//                "2024-11-01T10:05:00",
//                "2024-11-01T10:10:00",
//                50000L
//        );
//
//        // Mock 서비스 동작 설정
//        when(paymentService.getPayment(1L)).thenReturn(mockResponse);
//
//        // 테스트 수행
//        mockMvc.perform(get("/api/payments/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.paymentId").value("payment123"))
//                .andExpect(jsonPath("$.paymentType").value("CARD"))
//                .andExpect(jsonPath("$.requestedAt").value("2024-11-01T10:05:00"))
//                .andExpect(jsonPath("$.approvedAt").value("2024-11-01T10:10:00"))
//                .andExpect(jsonPath("$.amount").value(50000));
//
//        // Verify 서비스 호출 확인
//        verify(paymentService, times(1)).getPayment(1L);
//    }
//
//    @Test
//    void testGetPaymentsSuccess() throws Exception {
//        // Mock PaymentDetailResponse 리스트 생성
//        List<PaymentDetailResponse> mockResponseList = List.of(
//                new PaymentDetailResponse("payment123", "CARD", "2024-11-01T10:05:00", "2024-11-01T10:10:00", 50000L),
//                new PaymentDetailResponse("payment124", "BANK_TRANSFER", "2024-11-02T11:00:00", "2024-11-02T11:15:00", 100000L)
//        );
//
//        // Mock 서비스 동작 설정
//        when(paymentService.getPayments(1L)).thenReturn(mockResponseList);
//
//        // 테스트 수행
//        mockMvc.perform(get("/api/payments/customer/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].paymentId").value("payment123"))
//                .andExpect(jsonPath("$[0].paymentType").value("CARD"))
//                .andExpect(jsonPath("$[0].requestedAt").value("2024-11-01T10:05:00"))
//                .andExpect(jsonPath("$[0].approvedAt").value("2024-11-01T10:10:00"))
//                .andExpect(jsonPath("$[0].amount").value(50000))
//                .andExpect(jsonPath("$[1].paymentId").value("payment124"))
//                .andExpect(jsonPath("$[1].paymentType").value("BANK_TRANSFER"))
//                .andExpect(jsonPath("$[1].requestedAt").value("2024-11-02T11:00:00"))
//                .andExpect(jsonPath("$[1].approvedAt").value("2024-11-02T11:15:00"))
//                .andExpect(jsonPath("$[1].amount").value(100000));
//
//        // Verify 서비스 호출 확인
//        verify(paymentService, times(1)).getPayments(1L);
//    }
//
//}
