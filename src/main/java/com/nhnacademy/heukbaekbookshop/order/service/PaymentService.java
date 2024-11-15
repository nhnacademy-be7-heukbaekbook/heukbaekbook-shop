package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Payment;
import com.nhnacademy.heukbaekbookshop.order.domain.Refund;
import com.nhnacademy.heukbaekbookshop.order.domain.RefundStatus;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.*;
import com.nhnacademy.heukbaekbookshop.order.exception.PaymentFailureException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.PaymentRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.PaymentTypeRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.RefundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final RefundRepository refundRepository;
    private final CustomerRepository customerRepository;

    @Value("${toss.secret-key}")
    private String secretKey;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public PaymentService(RefundRepository refundRepository, CustomerRepository customerRepository, PaymentRepository paymentRepository, OrderRepository orderRepository, PaymentTypeRepository paymentTypeRepository, RestTemplate restTemplate) {
        this.refundRepository = refundRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
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


    public PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", request.paymentKey());
        requestBody.put("orderId", request.paymentOrderId());
        requestBody.put("amount", request.amount());

        TossPaymentApprovalResponse tossResponse = executePostRequest(url, requestBody, TossPaymentApprovalResponse.class);

//        // 결제 정보 생성 로직 추가
//        Order order = orderRepository.findById(request.orderId())
//                .orElseThrow(() -> new PaymentFailureException("주문 정보를 찾을 수 없습니다."));
//
//        PaymentType paymentType = paymentTypeRepository.findByName(tossResponse.method())
//                .orElseGet(() -> {
//                    PaymentType newPaymentType = new PaymentType();
//                    newPaymentType.setName(tossResponse.method());
//                    return paymentTypeRepository.save(newPaymentType);
//                });
//
//        Payment payment = new Payment();
//        payment.setOrder(order);
//        payment.setPaymentType(paymentType);
//        payment.setRequestedAt(LocalDateTime.parse(tossResponse.requestedAt()));
//        payment.setApprovedAt(LocalDateTime.parse(tossResponse.approvedAt()));
//        payment.setPrice(BigDecimal.valueOf(request.amount()));
//        // payment.setPaymentKey(tossResponse.paymentKey());
//
//        order.setStatus(OrderStatus.PAYMENT_COMPLETED);
//
//        paymentRepository.save(payment);

        return new PaymentApprovalResponse("결제에 성공하였습니다.");
    }

    public PaymentCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cancelReason", request.cancelReason());

        TossPaymentCancelResponse tossResponse = executePostRequest(url, requestBody, TossPaymentCancelResponse.class);

        Refund returnInfo = new Refund();
        returnInfo.setReason(request.cancelReason());
        returnInfo.setRefundRequestAt(OffsetDateTime.parse(tossResponse.requestedAt()).toLocalDateTime());
        returnInfo.setRefundApprovedAt(OffsetDateTime.parse(tossResponse.approvedAt()).toLocalDateTime());
        returnInfo.setRefundStatus(RefundStatus.REQUEST);

        refundRepository.save(returnInfo);

        return new PaymentCancelResponse("환불 요청이 접수되었습니다.");
    }

    public List<PaymentDetailResponse> getPayments(Long customerId) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new PaymentFailureException("고객 정보를 찾을 수 없습니다.");
        }

        return orderRepository.findByCustomerId(customerId).stream()
                .flatMap(order -> paymentRepository.findByOrderId(order.getId()).stream())
                .map(payment -> new PaymentDetailResponse(
                        payment.getId(),
                        payment.getPaymentType().getName(),
                        payment.getRequestedAt().toString(),
                        payment.getApprovedAt().toString(),
                        payment.getPrice().longValue()))
                .collect(Collectors.toList());
    }

    public PaymentDetailResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentFailureException("결제 정보를 찾을 수 없습니다."));

        return new PaymentDetailResponse(
                payment.getId(),
                payment.getPaymentType().getName(),
                payment.getRequestedAt().toString(),
                payment.getApprovedAt().toString(),
                payment.getPrice().longValue());
    }
}
