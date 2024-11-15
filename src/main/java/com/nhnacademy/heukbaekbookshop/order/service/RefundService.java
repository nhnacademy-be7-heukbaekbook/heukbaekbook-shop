package com.nhnacademy.heukbaekbookshop.order.service;

import com.nhnacademy.heukbaekbookshop.memberset.customer.exception.CustomerNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.customer.repository.CustomerRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBookRefund;
import com.nhnacademy.heukbaekbookshop.order.domain.Refund;
import com.nhnacademy.heukbaekbookshop.order.dto.response.RefundDetailResponse;
import com.nhnacademy.heukbaekbookshop.order.exception.RefundNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRefundRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderBookRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.order.repository.RefundRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final OrderBookRefundRepository orderBookRefundRepository;
    private final CustomerRepository customerRepository;

    public RefundService(RefundRepository refundRepository, OrderRepository orderRepository, OrderBookRepository orderBookRepository, OrderBookRefundRepository orderBookRefundRepository, CustomerRepository customerRepository) {
        this.refundRepository = refundRepository;
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.orderBookRefundRepository = orderBookRefundRepository;
        this.customerRepository = customerRepository;
    }

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

    public List<RefundDetailResponse> getRefunds(Long customerId) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new CustomerNotFoundException("환불 내역을 조회할 수 없습니다.");
        }
        List<RefundDetailResponse> refundDetailResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        for (Order order : orders) {
            List<String> titles = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            List<BigDecimal> prices = new ArrayList<>();
            List<OrderBookRefund> orderBookRefunds = orderBookRefundRepository.findByOrderId(order.getId());

            for (OrderBookRefund orderBookRefund : orderBookRefunds) {
                OrderBook orderBook = orderBookRepository.findByOrderIdAndBookId(orderBookRefund.getOrderId(), orderBookRefund.getBookId());
                Refund refund = refundRepository.findById(orderBookRefund.getRefundId()).get();
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
                        )
                );
            }

        }
        return refundDetailResponses;
    }
}

