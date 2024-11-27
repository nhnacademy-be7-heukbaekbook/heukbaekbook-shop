package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.exception.CustomerNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.mapper.MemberMapper;
import com.nhnacademy.heukbaekbookshop.memberset.member.dto.response.MemberResponse;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookRefund;
import com.nhnacademy.heukbaekbookshop.order.domain.Refund;
import com.nhnacademy.heukbaekbookshop.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekbookshop.order.dto.response.MyPageRefundDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.RefundNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRefundRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final PaymentService paymentService;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final OrderBookRefundRepository orderBookRefundRepository;
    private final MemberRepository memberRepository;

    public RefundDetailResponse getRefund(Long refundId) {
        if (refundRepository.findById(refundId).isEmpty()) {
            throw new RefundNotFoundException("환불 내역을 조회할 수 없습니다.");
        }
        List<OrderBookRefund> orderBookRefunds = orderBookRefundRepository.findByRefundId(refundId);
        List<String> titles = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<BigDecimal> prices = new ArrayList<>();
        Refund refund = refundRepository.findById(refundId).get();

        for (OrderBookRefund orderBookRefund : orderBookRefunds) {
            OrderBook orderBook = orderBookRepository.findByOrderIdAndBookId(orderBookRefund.getOrderId(), orderBookRefund.getBookId());
            titles.add(orderBook.getBook().getTitle());
            quantities.add(orderBookRefund.getQuantity());
            prices.add(orderBook.getPrice().multiply(BigDecimal.valueOf(orderBookRefund.getQuantity())));
        }

        return new RefundDetailResponse(
                refund.getId(),
                refund.getReason(),
                refund.getRefundRequestAt().toString(),
                refund.getRefundApprovedAt().toString(),
                refund.getRefundStatus().toString(),
                orderBookRefunds.getFirst().getOrderId(),
                titles,
                quantities,
                prices
        );
    }

    public MyPageRefundDetailResponse getRefunds(String userId) {
        Member member = memberRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomerNotFoundException("회원 정보를 찾을 수 없습니다."));

        List<RefundDetailResponse> refundDetailResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findByCustomerId(member.getId());

        for (Order order : orders) {
            List<OrderBookRefund> orderBookRefunds = orderBookRefundRepository.findByOrderId(order.getId());
            if (orderBookRefunds.isEmpty()) {
                continue;
            }
            for (OrderBookRefund orderBookRefund : orderBookRefunds) {
                OrderBook orderBook = orderBookRepository.findByOrderIdAndBookId(orderBookRefund.getOrderId(), orderBookRefund.getBookId());
                Refund refund = refundRepository.findById(orderBookRefund.getRefundId())
                        .orElseThrow(() -> new RefundNotFoundException("환불 정보를 찾을 수 없습니다."));
                List<String> titles = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                List<BigDecimal> prices = new ArrayList<>();

                titles.add(orderBook.getBook().getTitle());
                quantities.add(orderBookRefund.getQuantity());
                prices.add(orderBook.getPrice().multiply(BigDecimal.valueOf(orderBookRefund.getQuantity())));

                refundDetailResponses.add(new RefundDetailResponse(
                        refund.getId(),
                        refund.getReason(),
                        refund.getRefundRequestAt().toString(),
                        refund.getRefundApprovedAt().toString(),
                        refund.getRefundStatus().toString(),
                        orderBookRefund.getOrderId(),
                        titles,
                        quantities,
                        prices
                ));
            }
        }
        MemberResponse memberResponse = MemberMapper.createMemberResponse(memberRepository.findById(Long.parseLong(userId)).orElseThrow(MemberNotFoundException::new));

        return new MyPageRefundDetailResponse(memberResponse, refundDetailResponses);
    }

    public RefundCreateResponse createRefund(RefundCreateRequest request) {
        return paymentService.refundPayment(request);
    }
}

