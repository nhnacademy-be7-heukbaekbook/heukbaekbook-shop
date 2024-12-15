package com.nhnacademy.heukbaekbookshop.point.history.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.domain.mapper.PointHistoryMapper;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.exception.InsufficientPointsException;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointSaveServiceImpl implements PointSaveService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;



    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PointHistoryResponse createPointHistory(Long customerId, PointHistoryRequest pointHistoryRequest) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(MemberNotFoundException::new);

//        TODO ORDER
        Order order = null;
        if (pointHistoryRequest.orderId() != null) {
            order = orderRepository.findById(pointHistoryRequest.orderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + pointHistoryRequest.orderId()));
        }

        BigDecimal currentBalance = pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId)
                .map(PointHistory::getBalance)
                .orElse(BigDecimal.ZERO);
        BigDecimal adjustedAmount = calculateAdjustedAmount(pointHistoryRequest.amount(), pointHistoryRequest.type());
        BigDecimal newBalance = currentBalance.add(adjustedAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }

        PointHistory pointHistory = PointHistoryMapper.toEntity(pointHistoryRequest, member, order, newBalance);
        PointHistory savedPointHistory = pointHistoryRepository.save(pointHistory);
        return PointHistoryMapper.toResponse(savedPointHistory);
    }

    @Override
    public List<PointHistory> getPointHistories(Long customerId, Long orderId, PointType type) {
        return pointHistoryRepository.findByOrderIdAndType(orderId, type);
    }


    private BigDecimal calculateAdjustedAmount(BigDecimal amount, PointType type) {
        return switch (type) {
            case EARNED -> amount;
            case USED, CANCELLED -> amount.negate();
            default -> throw new IllegalArgumentException("Unsupported PointType: " + type);
        };
    }
}
