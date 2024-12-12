package com.nhnacademy.heukbaekbookshop.point.history.listener;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnType;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.earn.service.PointEarnStandardService;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.event.*;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointAccumulationListenerTest {

    @Mock
    PointEarnStandardService pointEarnStandardService;

    @Mock
    PointSaveService pointSaveService;

    @InjectMocks
    PointAccumulationListener listener;

    private PointEarnStandardResponse fixedStandard;
    private PointEarnStandardResponse percentStandard;

    @BeforeEach
    void setUp() {
        fixedStandard = new PointEarnStandardResponse(
                1L,
                "회원가입 포인트",
                BigDecimal.valueOf(1000),
                null,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                EventCode.SIGNUP
        );

        percentStandard = new PointEarnStandardResponse(
                2L,
                "주문 적립율",
                BigDecimal.valueOf(10), // 10%
                null,
                PointEarnType.PERCENTAGE,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                EventCode.ORDER
        );
    }

    @Test
    void handleSignupEvent() {
        when(pointEarnStandardService.getValidStandardsByEvent(EventCode.SIGNUP))
                .thenReturn(List.of(fixedStandard));

        SignupEvent event = new SignupEvent(100L);
        listener.handleSignupEvent(event);

        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PointHistoryRequest> requestCaptor = ArgumentCaptor.forClass(PointHistoryRequest.class);

        verify(pointSaveService, times(1))
                .createPointHistory(memberIdCaptor.capture(), requestCaptor.capture());
        assertEquals(100L, memberIdCaptor.getValue());
        assertEquals("회원가입", requestCaptor.getValue().pointName());
        assertEquals(BigDecimal.valueOf(1000), requestCaptor.getValue().amount());
        assertEquals(PointType.EARNED, requestCaptor.getValue().type());
    }

    @Test
    void handleOrderEvent() {
        when(pointEarnStandardService.getValidStandardsByEvent(EventCode.ORDER))
                .thenReturn(List.of(percentStandard));

        OrderEvent event = new OrderEvent(200L, 300L, BigDecimal.valueOf(20000));
        listener.handleOrderEvent(event);

        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PointHistoryRequest> requestCaptor = ArgumentCaptor.forClass(PointHistoryRequest.class);

        verify(pointSaveService, times(1))
                .createPointHistory(memberIdCaptor.capture(), requestCaptor.capture());

        assertEquals(200L, memberIdCaptor.getValue());
        assertEquals("주문", requestCaptor.getValue().pointName());
        // 10% of 20000 = 2000
        assertEquals(BigDecimal.valueOf(2000), requestCaptor.getValue().amount());
        assertEquals(PointType.EARNED, requestCaptor.getValue().type());
    }

    @Test
    void handlerReviewWithPhoto() {
        PointEarnStandardResponse photoReviewStandard = new PointEarnStandardResponse(
                3L,
                "포토 리뷰",
                BigDecimal.valueOf(500),
                null,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                EventCode.REVIEW_WITH_PHOTO
        );

        when(pointEarnStandardService.getValidStandardsByEvent(EventCode.REVIEW_WITH_PHOTO))
                .thenReturn(List.of(photoReviewStandard));

        ReviewEvent event = new ReviewEvent(300L, 400L, true);
        listener.handlerReview(event);

        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PointHistoryRequest> requestCaptor = ArgumentCaptor.forClass(PointHistoryRequest.class);

        verify(pointSaveService, times(1))
                .createPointHistory(memberIdCaptor.capture(), requestCaptor.capture());

        assertEquals(300L, memberIdCaptor.getValue());
        assertEquals("포토 리뷰", requestCaptor.getValue().pointName());
        assertEquals(BigDecimal.valueOf(500), requestCaptor.getValue().amount());
        assertEquals(PointType.EARNED, requestCaptor.getValue().type());
    }

    @Test
    void handlerReviewWithoutPhoto() {
        PointEarnStandardResponse normalReviewStandard = new PointEarnStandardResponse(
                4L,
                "리뷰",
                BigDecimal.valueOf(200),
                null,
                PointEarnType.FIXED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                EventCode.REVIEW_WITHOUT_PHOTO
        );

        when(pointEarnStandardService.getValidStandardsByEvent(EventCode.REVIEW_WITHOUT_PHOTO))
                .thenReturn(List.of(normalReviewStandard));

        ReviewEvent event = new ReviewEvent(300L, 400L, false);
        listener.handlerReview(event);

        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PointHistoryRequest> requestCaptor = ArgumentCaptor.forClass(PointHistoryRequest.class);

        verify(pointSaveService, times(1))
                .createPointHistory(memberIdCaptor.capture(), requestCaptor.capture());

        assertEquals(300L, memberIdCaptor.getValue());
        assertEquals("리뷰", requestCaptor.getValue().pointName());
        assertEquals(BigDecimal.valueOf(200), requestCaptor.getValue().amount());
        assertEquals(PointType.EARNED, requestCaptor.getValue().type());
    }

    @Test
    void handlePointUseEvent() {
        PointUseEvent event = new PointUseEvent(400L, 500L, BigDecimal.valueOf(1500));
        listener.handlePointUseEvent(event);

        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PointHistoryRequest> requestCaptor = ArgumentCaptor.forClass(PointHistoryRequest.class);

        verify(pointSaveService, times(1))
                .createPointHistory(memberIdCaptor.capture(), requestCaptor.capture());

        assertEquals(400L, memberIdCaptor.getValue());
        assertEquals("포인트 사용", requestCaptor.getValue().pointName());
        assertEquals(BigDecimal.valueOf(1500), requestCaptor.getValue().amount());
        assertEquals(PointType.USED, requestCaptor.getValue().type());
    }

    @Test
    void handleCancelEvent() {
        PointHistory earnedHistory = new PointHistory();
        earnedHistory.setAmount(BigDecimal.valueOf(2000));

        when(pointSaveService.getPointHistories(500L, 600L, PointType.EARNED))
                .thenReturn(Collections.singletonList(earnedHistory));

        CancelEvent event = new CancelEvent(500L, 600L);
        listener.handleCancelEvent(event);

        ArgumentCaptor<Long> memberIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PointHistoryRequest> requestCaptor = ArgumentCaptor.forClass(PointHistoryRequest.class);

        verify(pointSaveService, times(1))
                .createPointHistory(memberIdCaptor.capture(), requestCaptor.capture());

        assertEquals(500L, memberIdCaptor.getValue());
        assertEquals("주문 취소", requestCaptor.getValue().pointName());
        assertEquals(BigDecimal.valueOf(2000), requestCaptor.getValue().amount());
        assertEquals(PointType.CANCELLED, requestCaptor.getValue().type());
    }

}
