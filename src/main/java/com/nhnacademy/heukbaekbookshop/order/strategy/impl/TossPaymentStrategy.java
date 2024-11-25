package com.nhnacademy.heukbaekbookshop.order.strategy.impl;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentGatewayCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.TossPaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.TossPaymentCancelResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TossPaymentStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(TossPaymentStrategy.class);

    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate;

    public TossPaymentStrategy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getPaymentMethodName() {
        return "TOSS";
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "", StandardCharsets.UTF_8);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private <T> T executePostRequest(String url, Map<String, Object> requestBody, Class<T> responseType) {
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, createHttpHeaders());

        try {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    responseType
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                log.error("API 요청 실패: URL={}, 상태코드={}, 응답={}", url, responseEntity.getStatusCode(), responseEntity.getBody());
                throw new PaymentFailureException("API 요청 실패: " + responseEntity.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("API 요청 중 클라이언트 오류 발생: URL={}, 응답={}", url, e.getResponseBodyAsString());
            throw new PaymentFailureException("API 요청 실패: " + e.getMessage());
        } catch (Exception e) {
            log.error("API 요청 중 예외 발생: URL={}, 예외={}", url, e.getMessage());
            throw new PaymentFailureException("API 요청 실패: " + e.getMessage());
        }
    }

    @Override
    public PaymentGatewayApprovalResponse approvePayment(PaymentApprovalRequest request) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", request.paymentKey());
        requestBody.put("orderId", request.orderId());
        requestBody.put("amount", request.amount());

        TossPaymentApprovalResponse tossResponse = executePostRequest(url, requestBody, TossPaymentApprovalResponse.class);

        return new PaymentGatewayApprovalResponse(
                tossResponse.paymentKey(),
                tossResponse.requestedAt(),
                tossResponse.approvedAt(),
                tossResponse.totalAmount(),
                tossResponse.method()
        );
    }

    @Override
    public PaymentGatewayCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cancelReason", request.cancelReason());

        TossPaymentCancelResponse tossResponse = executePostRequest(url, requestBody, TossPaymentCancelResponse.class);

        return new PaymentGatewayCancelResponse(
                tossResponse.requestedAt(),
                tossResponse.approvedAt(),
                "결제 취소 요청이 접수되었습니다."
        );
    }
}
