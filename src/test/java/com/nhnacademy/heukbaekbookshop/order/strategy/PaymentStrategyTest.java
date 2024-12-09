package com.nhnacademy.heukbaekbookshop.order.strategy;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.TossPaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.TossPaymentCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.strategy.impl.PayPalPaymentStrategy;
import com.nhnacademy.heukbaekbookshop.order.strategy.impl.TossPaymentStrategy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PaymentStrategyTest.TestConfig.class)
class PaymentStrategyTest {

    @Autowired
    private TossPaymentStrategy tossPaymentStrategy;

    @Autowired
    private PayPalPaymentStrategy payPalPaymentStrategy;

    @MockBean
    private RestTemplate restTemplate;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TossPaymentStrategy tossPaymentStrategy(RestTemplate restTemplate) {
            return new TossPaymentStrategy(restTemplate);
        }

        @Bean
        public PayPalPaymentStrategy payPalPaymentStrategy() {
            return new PayPalPaymentStrategy();
        }

        @Bean
        public RestTemplate restTemplate() {
            return Mockito.mock(RestTemplate.class);
        }
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("toss.secret-key", () -> "testSecretKey");
        registry.add("paypal.secret-key", () -> "testPayPalSecretKey");
    }

    @Nested
    class TossPaymentStrategyTests {

        @Test
        void testGetPaymentMethodName() {
            String methodName = tossPaymentStrategy.getPaymentMethodName();
            assertEquals("TOSS", methodName, "결제 방법 이름이 'TOSS'이어야 합니다.");
        }

        @Test
        void testApprovePayment_Success() {
            PaymentApprovalRequest request = new PaymentApprovalRequest("paymentKey123", "orderId123", 100L, "CARD");

            Map<String, Object> expectedRequestBody = Map.of(
                    "paymentKey", "paymentKey123",
                    "orderId", "orderId123",
                    "amount", 100L
            );

            TossPaymentApprovalResponse mockResponse = new TossPaymentApprovalResponse(
                    "paymentKey123",
                    "2023-12-04T12:00:00",
                    "2023-12-04T12:05:00",
                    new BigDecimal("100.00"),
                    "CARD"
            );

            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    any(HttpEntity.class),
                    eq(TossPaymentApprovalResponse.class)
            )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

            ArgumentCaptor<HttpEntity<Map<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);

            PaymentGatewayApprovalResponse response = tossPaymentStrategy.approvePayment(request);

            assertNotNull(response, "결제 승인 응답은 null이 아니어야 합니다.");
            assertEquals(mockResponse.paymentKey(), response.paymentKey(), "paymentKey가 일치해야 합니다.");
            assertEquals(mockResponse.requestedAt(), response.requestedAt(), "requestedAt이 일치해야 합니다.");
            assertEquals(mockResponse.approvedAt(), response.approvedAt(), "approvedAt이 일치해야 합니다.");
            assertEquals(mockResponse.totalAmount(), response.cancelAmount(), "cancelAmount가 일치해야 합니다.");
            assertEquals(mockResponse.method(), response.method(), "method가 일치해야 합니다.");

            verify(restTemplate, times(1)).postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    captor.capture(),
                    eq(TossPaymentApprovalResponse.class)
            );

            HttpEntity<Map<String, Object>> capturedEntity = captor.getValue();

            HttpHeaders headers = capturedEntity.getHeaders();
            assertTrue(headers.getContentType().includes(MediaType.APPLICATION_JSON), "Content-Type이 application/json이어야 합니다.");
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            assertNotNull(authHeader, "Authorization 헤더가 존재해야 합니다.");
            String expectedAuth = "Basic " + Base64.getEncoder().encodeToString(("testSecretKey:").getBytes(StandardCharsets.UTF_8));
            assertEquals(expectedAuth, authHeader, "Authorization 헤더가 올바르게 설정되어야 합니다.");

            Map<String, Object> body = capturedEntity.getBody();
            assertNotNull(body, "요청 본문이 null이 아니어야 합니다.");
            assertEquals(expectedRequestBody, body, "요청 본문이 일치해야 합니다.");
        }

        @Test
        void testApprovePayment_HttpClientError() {
            PaymentApprovalRequest request = new PaymentApprovalRequest("invalidPaymentKey", "orderId123", 100L, "CARD");

            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    any(HttpEntity.class),
                    eq(TossPaymentApprovalResponse.class)
            )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

            PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
                tossPaymentStrategy.approvePayment(request);
            }, "PaymentFailureException이 발생해야 합니다.");

            assertTrue(exception.getMessage().contains("API 요청 실패"), "예외 메시지가 올바르게 설정되어야 합니다.");
        }

        @Test
        void testApprovePayment_OtherException() {
            PaymentApprovalRequest request = new PaymentApprovalRequest("paymentKey123", "orderId123", 100L, "CARD");

            when(restTemplate.postForEntity(
                    eq("https://api.tosspayments.com/v1/payments/confirm"),
                    any(HttpEntity.class),
                    eq(TossPaymentApprovalResponse.class)
            )).thenThrow(new RuntimeException("Internal Server Error"));

            PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
                tossPaymentStrategy.approvePayment(request);
            }, "PaymentFailureException이 발생해야 합니다.");

            assertTrue(exception.getMessage().contains("API 요청 실패"), "예외 메시지가 올바르게 설정되어야 합니다.");
        }

        @Test
        void testRefundPayment_Success() {
            RefundBookRequest refundBook1 = new RefundBookRequest(1L, 2, new BigDecimal("20.00"));
            RefundBookRequest refundBook2 = new RefundBookRequest(2L, 1, new BigDecimal("30.00"));
            List<RefundBookRequest> refundBooks = List.of(refundBook1, refundBook2);
            RefundCreateRequest request = new RefundCreateRequest(refundBooks, "paymentKey123", "Cancel Reason", "TOSS");

            BigDecimal expectedCancelAmount = new BigDecimal("70.00");

            Map<String, Object> expectedRequestBody = Map.of(
                    "cancelAmount", expectedCancelAmount,
                    "cancelReason", "Cancel Reason"
            );

            TossPaymentCancelResponse mockResponse = new TossPaymentCancelResponse(
                    "2023-12-04T12:00:00",
                    "2023-12-04T12:05:00",
                    "orderId123",
                    expectedCancelAmount
            );

            String cancelUrl = "https://api.tosspayments.com/v1/payments/paymentKey123/cancel";

            when(restTemplate.postForEntity(
                    eq(cancelUrl),
                    any(HttpEntity.class),
                    eq(TossPaymentCancelResponse.class)
            )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

            ArgumentCaptor<HttpEntity<Map<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);

            PaymentGatewayCancelResponse response = tossPaymentStrategy.refundPayment(request);

            assertNotNull(response, "결제 취소 응답은 null이 아니어야 합니다.");
            assertEquals(mockResponse.requestedAt(), response.requestedAt(), "requestedAt이 일치해야 합니다.");
            assertEquals(mockResponse.approvedAt(), response.approvedAt(), "approvedAt이 일치해야 합니다.");
            assertEquals(mockResponse.orderId(), response.orderId(), "orderId가 일치해야 합니다.");
            assertEquals(mockResponse.cancelAmount(), response.cancelAmount(), "cancelAmount가 일치해야 합니다.");
            assertEquals("TOSS", response.message(), "message가 'TOSS'이어야 합니다.");

            verify(restTemplate, times(1)).postForEntity(
                    eq(cancelUrl),
                    captor.capture(),
                    eq(TossPaymentCancelResponse.class)
            );

            HttpEntity<Map<String, Object>> capturedEntity = captor.getValue();

            HttpHeaders headers = capturedEntity.getHeaders();
            assertTrue(headers.getContentType().includes(MediaType.APPLICATION_JSON), "Content-Type이 application/json이어야 합니다.");
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            assertNotNull(authHeader, "Authorization 헤더가 존재해야 합니다.");
            String expectedAuth = "Basic " + Base64.getEncoder().encodeToString(("testSecretKey:").getBytes(StandardCharsets.UTF_8));
            assertEquals(expectedAuth, authHeader, "Authorization 헤더가 올바르게 설정되어야 합니다.");

            Map<String, Object> body = capturedEntity.getBody();
            assertNotNull(body, "요청 본문이 null이 아니어야 합니다.");
            assertEquals(expectedRequestBody, body, "요청 본문이 일치해야 합니다.");
        }

        @Test
        void testRefundPayment_HttpClientError() {
            RefundCreateRequest request = new RefundCreateRequest(List.of(), "invalidPaymentKey", "Cancel Reason", "TOSS");

            String cancelUrl = "https://api.tosspayments.com/v1/payments/invalidPaymentKey/cancel";

            when(restTemplate.postForEntity(
                    eq(cancelUrl),
                    any(HttpEntity.class),
                    eq(TossPaymentCancelResponse.class)
            )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

            PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
                tossPaymentStrategy.refundPayment(request);
            }, "PaymentFailureException이 발생해야 합니다.");

            assertTrue(exception.getMessage().contains("API 요청 실패"), "예외 메시지가 올바르게 설정되어야 합니다.");
        }

        @Test
        void testRefundPayment_OtherException() {
            RefundCreateRequest request = new RefundCreateRequest(List.of(), "paymentKey123", "Cancel Reason", "TOSS");

            String cancelUrl = "https://api.tosspayments.com/v1/payments/paymentKey123/cancel";

            when(restTemplate.postForEntity(
                    eq(cancelUrl),
                    any(HttpEntity.class),
                    eq(TossPaymentCancelResponse.class)
            )).thenThrow(new RuntimeException("Internal Server Error"));

            PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
                tossPaymentStrategy.refundPayment(request);
            }, "PaymentFailureException이 발생해야 합니다.");

            assertTrue(exception.getMessage().contains("API 요청 실패"), "예외 메시지가 올바르게 설정되어야 합니다.");
        }

        @Test
        void testCancelPayment_Success() {
            PaymentCancelRequest request = new PaymentCancelRequest("paymentKey123", "Cancel Reason", "TOSS");

            Map<String, Object> expectedRequestBody = Map.of(
                    "cancelReason", "Cancel Reason"
            );

            TossPaymentCancelResponse mockResponse = new TossPaymentCancelResponse(
                    "2023-12-04T12:00:00",
                    "2023-12-04T12:05:00",
                    "orderId123",
                    BigDecimal.ZERO
            );

            String cancelUrl = "https://api.tosspayments.com/v1/payments/paymentKey123/cancel";

            when(restTemplate.postForEntity(
                    eq(cancelUrl),
                    any(HttpEntity.class),
                    eq(TossPaymentCancelResponse.class)
            )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

            ArgumentCaptor<HttpEntity<Map<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);

            PaymentGatewayCancelResponse response = tossPaymentStrategy.cancelPayment(request);

            assertNotNull(response, "결제 취소 응답은 null이 아니어야 합니다.");
            assertEquals(mockResponse.requestedAt(), response.requestedAt(), "requestedAt이 일치해야 합니다.");
            assertEquals(mockResponse.approvedAt(), response.approvedAt(), "approvedAt이 일치해야 합니다.");
            assertEquals(mockResponse.orderId(), response.orderId(), "orderId가 일치해야 합니다.");
            assertEquals(mockResponse.cancelAmount(), response.cancelAmount(), "cancelAmount가 일치해야 합니다.");
            assertEquals("TOSS", response.message(), "message가 'TOSS'이어야 합니다.");

            verify(restTemplate, times(1)).postForEntity(
                    eq(cancelUrl),
                    captor.capture(),
                    eq(TossPaymentCancelResponse.class)
            );

            HttpEntity<Map<String, Object>> capturedEntity = captor.getValue();

            HttpHeaders headers = capturedEntity.getHeaders();
            assertTrue(headers.getContentType().includes(MediaType.APPLICATION_JSON), "Content-Type이 application/json이어야 합니다.");
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            assertNotNull(authHeader, "Authorization 헤더가 존재해야 합니다.");
            String expectedAuth = "Basic " + Base64.getEncoder().encodeToString(("testSecretKey:").getBytes(StandardCharsets.UTF_8));
            assertEquals(expectedAuth, authHeader, "Authorization 헤더가 올바르게 설정되어야 합니다.");

            Map<String, Object> body = capturedEntity.getBody();
            assertNotNull(body, "요청 본문이 null이 아니어야 합니다.");
            assertEquals(expectedRequestBody, body, "요청 본문이 일치해야 합니다.");
        }

        @Test
        void testCancelPayment_HttpClientError() {
            PaymentCancelRequest request = new PaymentCancelRequest("invalidPaymentKey", "Cancel Reason", "TOSS");

            String cancelUrl = "https://api.tosspayments.com/v1/payments/invalidPaymentKey/cancel";

            when(restTemplate.postForEntity(
                    eq(cancelUrl),
                    any(HttpEntity.class),
                    eq(TossPaymentCancelResponse.class)
            )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

            PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
                tossPaymentStrategy.cancelPayment(request);
            }, "PaymentFailureException이 발생해야 합니다.");

            assertTrue(exception.getMessage().contains("API 요청 실패"), "예외 메시지가 올바르게 설정되어야 합니다.");
        }

        @Test
        void testCancelPayment_OtherException() {
            PaymentCancelRequest request = new PaymentCancelRequest("paymentKey123", "Cancel Reason", "TOSS");

            String cancelUrl = "https://api.tosspayments.com/v1/payments/paymentKey123/cancel";

            when(restTemplate.postForEntity(
                    eq(cancelUrl),
                    any(HttpEntity.class),
                    eq(TossPaymentCancelResponse.class)
            )).thenThrow(new RuntimeException("Internal Server Error"));

            PaymentFailureException exception = assertThrows(PaymentFailureException.class, () -> {
                tossPaymentStrategy.cancelPayment(request);
            }, "PaymentFailureException이 발생해야 합니다.");

            assertTrue(exception.getMessage().contains("API 요청 실패"), "예외 메시지가 올바르게 설정되어야 합니다.");
        }
    }

    @Nested
    class PayPalPaymentStrategyTests {

        @Test
        void testGetPaymentMethodName() {
            String methodName = payPalPaymentStrategy.getPaymentMethodName();
            assertEquals("PAYPAL", methodName, "결제 방법 이름이 'PAYPAL'이어야 합니다.");
        }

        @Test
        void testApprovePayment_ReturnsNull() {
            PaymentApprovalRequest request = new PaymentApprovalRequest("paymentKey123", "orderId123", 100L, "PAYPAL");

            PaymentGatewayApprovalResponse response = payPalPaymentStrategy.approvePayment(request);
            assertNull(response, "approvePayment은 null을 반환해야 합니다.");
        }

        @Test
        void testRefundPayment_ReturnsNull() {
            RefundCreateRequest request = new RefundCreateRequest(List.of(), "paymentKey123", "Cancel Reason", "PAYPAL");

            PaymentGatewayCancelResponse response = payPalPaymentStrategy.refundPayment(request);
            assertNull(response, "refundPayment은 null을 반환해야 합니다.");
        }

        @Test
        void testCancelPayment_ReturnsNull() {
            PaymentCancelRequest request = new PaymentCancelRequest("paymentKey123", "Cancel Reason", "PAYPAL");

            PaymentGatewayCancelResponse response = payPalPaymentStrategy.cancelPayment(request);
            assertNull(response, "cancelPayment은 null을 반환해야 합니다.");
        }
    }
}