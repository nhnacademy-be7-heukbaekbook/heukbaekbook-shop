package com.nhnacademy.heukbaekbookshop.point.history.service.impl;

import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import com.nhnacademy.heukbaekbookshop.order.domain.Order;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.repository.OrderRepository;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.dto.response.PointHistoryResponse;
import com.nhnacademy.heukbaekbookshop.point.history.exception.InsufficientPointsException;
import com.nhnacademy.heukbaekbookshop.point.history.repository.PointHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointSaveServiceImplTest {

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PointSaveServiceImpl pointSaveService;

    @Test
    void testCreatePointHistory_Success() {
        Long customerId = 1L;
        Long orderId = 2L;
        Member mockMember = mock(Member.class);
        Order mockOrder = mock(Order.class);
        PointHistoryRequest request = new PointHistoryRequest(orderId, "Earn Points", BigDecimal.valueOf(100), LocalDateTime.now(), PointType.EARNED);

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(mockMember));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId)).thenReturn(Optional.empty());
        when(pointHistoryRepository.save(any(PointHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PointHistoryResponse response = pointSaveService.createPointHistory(customerId, request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(100), response.amount());
        assertEquals(PointType.EARNED, response.type());
        verify(pointHistoryRepository).save(any(PointHistory.class));
    }

    @Test
    void testCreatePointHistory_MemberNotFound() {
        Long customerId = 1L;
        PointHistoryRequest request = new PointHistoryRequest(null, "Earn Points", BigDecimal.valueOf(100), LocalDateTime.now(), PointType.EARNED);

        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> pointSaveService.createPointHistory(customerId, request));
    }

    @Test
    void testCreatePointHistory_OrderNotFound() {
        Long customerId = 1L;
        Long orderId = 2L;
        Member mockMember = mock(Member.class);
        PointHistoryRequest request = new PointHistoryRequest(orderId, "Earn Points", BigDecimal.valueOf(100), LocalDateTime.now(), PointType.EARNED);

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(mockMember));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> pointSaveService.createPointHistory(customerId, request));
    }

    @Test
    void testCreatePointHistory_InsufficientPoints() {
        Long customerId = 1L;
        Member mockMember = mock(Member.class);
        PointHistoryRequest request = new PointHistoryRequest(null, "Use Points", BigDecimal.valueOf(200), LocalDateTime.now(), PointType.USED);

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(mockMember));
        when(pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId))
                .thenReturn(Optional.of(new PointHistory(null, mockMember, null, BigDecimal.valueOf(100), LocalDateTime.now(), BigDecimal.valueOf(100), PointType.EARNED, "Initial Points")));

        assertThrows(InsufficientPointsException.class, () -> pointSaveService.createPointHistory(customerId, request));
    }

    @Test
    void testCreatePointHistory_Success_WithNegativePointsUsed() {
        Long customerId = 1L;
        Member mockMember = mock(Member.class);
        PointHistoryRequest request = new PointHistoryRequest(null, "Use Points", BigDecimal.valueOf(50), LocalDateTime.now(), PointType.USED);

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(mockMember));
        when(pointHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(customerId))
                .thenReturn(Optional.of(new PointHistory(null, mockMember, null, BigDecimal.valueOf(100), LocalDateTime.now(), BigDecimal.valueOf(100), PointType.EARNED, "Initial Points")));
        when(pointHistoryRepository.save(any(PointHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PointHistoryResponse response = pointSaveService.createPointHistory(customerId, request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(50), response.amount());
        assertEquals(BigDecimal.valueOf(50), response.balance());
        assertEquals(PointType.USED, response.type());
    }

    @Test
    void testGetPointHistories_Success() {
        // Arrange
        Long customerId = 1L;
        Long orderId = 2L;
        PointType type = PointType.EARNED;

        PointHistory mockHistory1 = mock(PointHistory.class);
        PointHistory mockHistory2 = mock(PointHistory.class);
        when(pointHistoryRepository.findByOrderIdAndType(orderId, type)).thenReturn(List.of(mockHistory1, mockHistory2));

        // Act
        List<PointHistory> result = pointSaveService.getPointHistories(customerId, orderId, type);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pointHistoryRepository).findByOrderIdAndType(orderId, type);
    }
}
