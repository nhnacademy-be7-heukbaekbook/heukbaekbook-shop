package com.nhnacademy.heukbaekbookshop.point.history.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.mapper.PointHistoryMapper;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
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

        return pointHistoryRepository.findByMemberId(customerId, pageable).map(PointHistoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCurrentBalanceByCustomerId(Long customerId) {
        validateMemberExists(customerId);

        return pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId)
                .map(PointHistory::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    private void validateMemberExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }
}
