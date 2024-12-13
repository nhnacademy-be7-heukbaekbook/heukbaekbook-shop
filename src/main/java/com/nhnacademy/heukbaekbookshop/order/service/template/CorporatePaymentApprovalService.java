package com.nhnacademy.heukbaekbookshop.order.service.template;

import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.dto.response.PaymentDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.repository.*;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorporatePaymentApprovalService extends AbstractPaymentApprovalService {

    public CorporatePaymentApprovalService(RefundRepository refundRepository,
                                           CustomerRepository customerRepository,
                                           PaymentRepository paymentRepository,
                                           OrderRepository orderRepository,
                                           PaymentTypeRepository paymentTypeRepository,
                                           OrderBookRefundRepository orderBookRefundRepository,
                                           MemberRepository memberRepository,
                                           ApplicationEventPublisher eventPublisher,
                                           PaymentStrategyFactory paymentStrategyFactory) {
        super(refundRepository, customerRepository, paymentRepository, orderRepository, 
              paymentTypeRepository, orderBookRefundRepository, memberRepository, 
              eventPublisher, paymentStrategyFactory);
    }

    /**
     * 기업 고객에 대해서는 포인트 적립 이벤트 대신
     * 다른 이벤트가 발생할 수 있다고 가정
     */


    @Override
    protected void afterPaymentApproved(Order order) {
        // 템플릿 메서드 패턴을 사용한 만큼, 전체 흐름은 유지한채 세부 구현을 재정의할 수 있음
    }

    public List<PaymentDetailResponse> getPayments(Long customerId) {
        return null;
    }

    public PaymentDetailResponse getPayment(String paymentId) {
        return null;
    }

}
