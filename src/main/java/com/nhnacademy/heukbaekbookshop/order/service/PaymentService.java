package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.TossPaymentResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.PaymentRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.PaymentTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Value("${toss.secret-key}")
    private String secretKey;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, PaymentTypeRepository paymentTypeRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentTypeRepository = paymentTypeRepository;
    }

    public PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "", StandardCharsets.UTF_8);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", request.paymentKey());
        requestBody.put("orderId", request.orderId());
        requestBody.put("amount", request.amount());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TossPaymentResponse> responseEntity = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    httpEntity,
                    TossPaymentResponse.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                TossPaymentResponse tossResponse = responseEntity.getBody();

                // 결제 정보 생성

                return new PaymentApprovalResponse(tossResponse.method(), true, tossResponse.toString());
            } else {
                log.error("결제 승인 실패: {}", responseEntity.getBody());
                throw new PaymentFailureException("결제 승인 실패");
            }
        } catch (HttpClientErrorException e) {
            log.error("결제 승인 요청 중 오류 발생: {}", e.getResponseBodyAsString());
            throw new PaymentFailureException("결제 승인 실패: " + e.getMessage());
        } catch (Exception e) {
            log.error("결제 승인 처리 중 예외 발생: {}", e.getMessage());
            throw new PaymentFailureException("결제 승인 실패: " + e.getMessage());
        }
    }
}
