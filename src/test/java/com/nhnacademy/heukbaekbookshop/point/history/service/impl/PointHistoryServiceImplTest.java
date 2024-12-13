package com.nhnacademy.heukbaekbookshop.point.history.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceImplTest {

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PointHistoryServiceImpl pointHistoryService;

    private Long memberId;
    private Pageable pageable;
    private PointHistory pointHistory;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        pointHistory = new PointHistory();
        pointHistory.setId(1L);
        pointHistory.setAmount(new BigDecimal("1000"));
        pointHistory.setBalance(new BigDecimal("5000"));
        pointHistory.setCreatedAt(LocalDateTime.now());
        pointHistory.setType(PointType.EARNED);
        pointHistory.setName("적립 포인트");
        pointHistory.setMember(new Member());
    }

    @Test
    void testGetPointHistoriesByCustomerId_Success() {
        given(memberRepository.existsById(memberId)).willReturn(true);

        Page<PointHistory> pointHistoryPage = new PageImpl<>(java.util.List.of(pointHistory), pageable, 1);
        given(pointHistoryRepository.findByMemberId(memberId, pageable)).willReturn(pointHistoryPage);

        Page<PointHistoryResponse> result = pointHistoryService.getPointHistoriesByCustomerId(memberId, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).amount()).isEqualByComparingTo("1000");
        assertThat(result.getContent().get(0).balance()).isEqualByComparingTo("5000");
    }

    @Test
    void testGetPointHistoriesByCustomerId_MemberNotFound() {
        given(memberRepository.existsById(memberId)).willReturn(false);

        assertThatThrownBy(() -> pointHistoryService.getPointHistoriesByCustomerId(memberId, pageable))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void testGetCurrentBalanceByCustomerId_Success() {
        given(memberRepository.existsById(memberId)).willReturn(true);
        given(pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId))
                .willReturn(Optional.of(pointHistory));

        BigDecimal result = pointHistoryService.getCurrentBalanceByCustomerId(memberId);

        assertThat(result).isEqualByComparingTo("5000");
    }

    @Test
    void testGetCurrentBalanceByCustomerId_NoHistory() {
        given(memberRepository.existsById(memberId)).willReturn(true);
        given(pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId))
                .willReturn(Optional.empty());

        BigDecimal result = pointHistoryService.getCurrentBalanceByCustomerId(memberId);

        assertThat(result).isEqualByComparingTo("0");
    }

    @Test
    void testGetCurrentBalanceByCustomerId_MemberNotFound() {
        given(memberRepository.existsById(memberId)).willReturn(false);

        assertThatThrownBy(() -> pointHistoryService.getCurrentBalanceByCustomerId(memberId))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
