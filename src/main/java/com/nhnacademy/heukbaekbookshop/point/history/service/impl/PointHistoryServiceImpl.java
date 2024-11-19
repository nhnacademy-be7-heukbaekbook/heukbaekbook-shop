package com.nhnacademy.heukbaekbookshop.point.history.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.domain.mapper.PointHistoryMapper;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.exception.InsufficientPointsException;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getPointHistoriesByCustomerId(Long customerId, Pageable pageable) {
        validateMemberExists(customerId);

        return pointHistoryRepository.findByMemberIdOrderByCreatedAtDesc(customerId, pageable).map(PointHistoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCurrentBalanceByCustomerId(Long customerId) {
        validateMemberExists(customerId);

        return pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId)
                .map(PointHistory::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public PointHistoryResponse createPointHistory(Long customerId, PointHistoryRequest pointHistoryRequest) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(MemberNotFoundException::new);

//        TODO ORDER
//        Order order = null;
//        if (request.orderId() != null) {
//            order = orderRepository.findById(request.orderId())
//                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + request.orderId()));
//        }

        BigDecimal currentBalance = pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId)
                .map(PointHistory::getBalance)
                .orElse(BigDecimal.ZERO);
        BigDecimal adjustedAmount = calculateAdjustedAmount(pointHistoryRequest.amount(), pointHistoryRequest.type());
        BigDecimal newBalance = currentBalance.add(adjustedAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }

        PointHistory pointHistory = PointHistoryMapper.toEntity(pointHistoryRequest, member, null, newBalance);
        PointHistory savedPointHistory = pointHistoryRepository.save(pointHistory);

        return PointHistoryMapper.toResponse(savedPointHistory);
    }

    private void validateMemberExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }

    private BigDecimal calculateAdjustedAmount(BigDecimal amount, PointType type) {
        return switch (type) {
            case EARNED, CANCELLED -> amount;
            case USED -> amount.negate();
            default -> throw new IllegalArgumentException("Unsupported PointType: " + type);
        };
    }
}
