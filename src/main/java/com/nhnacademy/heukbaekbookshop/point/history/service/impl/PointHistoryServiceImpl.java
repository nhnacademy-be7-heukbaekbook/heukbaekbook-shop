package com.nhnacademy.heukbaekbookshop.point.history.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.mapper.PointHistoryMapper;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    @Override
    public Page<PointHistoryResponse> getPointHistoriesByCustomerId(Long customerId, Pageable pageable) {
        if (!memberRepository.existsById(customerId)) {
            throw new MemberNotFoundException();
        }

        return pointHistoryRepository.findByMemberIdOrderByCreatedAtDesc(customerId, pageable).map(PointHistoryMapper::toResponse);
    }
}
