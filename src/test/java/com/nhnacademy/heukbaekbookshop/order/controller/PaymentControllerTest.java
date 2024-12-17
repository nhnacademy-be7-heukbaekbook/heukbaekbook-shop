package com.nhnacademy.heukbaekbookshop.order.controller;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void confirmPayment_ShouldReturnSuccessResponse() throws Exception {
        // PaymentApprovalRequest에 맞는 Mock 요청 생성
        PaymentApprovalRequest request = new PaymentApprovalRequest("paymentKey", "orderId", 100L, "CARD");

        // PaymentApprovalResponse에 맞는 Mock 응답 생성
        PaymentApprovalResponse response = new PaymentApprovalResponse(
                "2023-12-12T10:00:00",
                "COMPLETED",
                "John Doe",
                "010-1234-5678",
                "john.doe@example.com",
                "CARD",
                "2023-12-12T09:00:00",
                "2023-12-12T10:00:00",
                100,
                "Payment successful"
        );

        // Mock 서비스 설정
        when(paymentService.approvePayment(Mockito.any(PaymentApprovalRequest.class))).thenReturn(response);

        // MockMvc로 POST 요청 및 응답 검증
        mockMvc.perform(post("/api/payments/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "paymentKey": "paymentKey",
                                    "orderId": "orderId",
                                    "amount": 100,
                                    "method": "CARD"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCreatedAt").value("2023-12-12T10:00:00"))
                .andExpect(jsonPath("$.orderStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.orderCustomerName").value("John Doe"))
                .andExpect(jsonPath("$.orderCustomerPhoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.orderCustomerEmail").value("john.doe@example.com"))
                .andExpect(jsonPath("$.paymentType").value("CARD"))
                .andExpect(jsonPath("$.paymentRequestedAt").value("2023-12-12T09:00:00"))
                .andExpect(jsonPath("$.paymentApprovedAt").value("2023-12-12T10:00:00"))
                .andExpect(jsonPath("$.paymentPrice").value(100))
                .andExpect(jsonPath("$.message").value("Payment successful"));
    }

    @Test
    void confirmPayment_ShouldReturnFailureResponse() throws Exception {
        // Mock 서비스에서 예외 발생 설정
        Mockito.doThrow(new RuntimeException("Payment failed"))
                .when(paymentService).approvePayment(Mockito.any(PaymentApprovalRequest.class));

        mockMvc.perform(post("/api/payments/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "paymentKey": "paymentKey",
                                    "orderId": "orderId",
                                    "amount": 100,
                                    "method": "CARD"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("결제에 실패하였습니다."));
    }
}