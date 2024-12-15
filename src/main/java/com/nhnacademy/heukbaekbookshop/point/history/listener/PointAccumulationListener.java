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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointAccumulationListener {
    private final PointEarnStandardService pointEarnStandardService;
    private final PointSaveService pointSaveService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSignupEvent(SignupEvent event) {
        processEarnEvent(EventCode.SIGNUP, null, event.customerId(), BigDecimal.ZERO);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        processEarnEvent(EventCode.ORDER, event.orderId(), event.customerId(), event.orderAmount());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlerReview(ReviewEvent event) {
        if (event.hasPhoto()) {
            processEarnEvent(EventCode.REVIEW_WITH_PHOTO, event.orderId(), event.customerId(), BigDecimal.ZERO);
        } else {
            processEarnEvent(EventCode.REVIEW_WITHOUT_PHOTO, event.orderId(), event.customerId(), BigDecimal.ZERO);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePointUseEvent(PointUseEvent event) {
        processUseEvent(event.orderId(), event.customerId(), event.usePointAmount());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCancelEvent(CancelEvent event) {
        processCancelEvent(event.orderId(), event.customerId());
    }

    private void processEarnEvent(EventCode eventCode, Long orderId, Long userId, BigDecimal orderAmount) {
        List<PointEarnStandardResponse> standards = pointEarnStandardService.getValidStandardsByEvent(eventCode);

        for (PointEarnStandardResponse standard : standards) {
            try {
                BigDecimal points = calculatePoints(standard, orderAmount);
                PointHistoryRequest pointHistoryRequest = new PointHistoryRequest(
                        orderId,
                        eventCode.getDisplayName(),
                        points,
                        LocalDateTime.now(),
                        PointType.EARNED
                );
                pointSaveService.createPointHistory(userId, pointHistoryRequest);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("Point 적립 실패, OrderId: {}, MemberId: {}", orderId, userId);
            }
        }
    }

    private void processUseEvent(Long orderId, Long userId, BigDecimal usePointAmount) {
        PointHistoryRequest pointHistoryRequest = new PointHistoryRequest(
                orderId,
                "포인트 사용",
                usePointAmount,
                LocalDateTime.now(),
                PointType.USED
        );

        pointSaveService.createPointHistory(userId, pointHistoryRequest);
    }

    private void processCancelEvent(Long orderId, Long userId) {
        List<PointHistory> histories = pointSaveService.getPointHistories(userId, orderId, PointType.EARNED);

        for (PointHistory history : histories) {
            try {
                PointHistoryRequest cancelRequest = new PointHistoryRequest(
                        orderId,
                        "주문 취소",
                        history.getAmount(),
                        LocalDateTime.now(),
                        PointType.CANCELLED
                );
                pointSaveService.createPointHistory(userId, cancelRequest);
            } catch (Exception e) {
                log.error("Point 적립 취소 실패, OrderId: {}, MemberId: {}", orderId, userId);
            }
        }
    }

    private BigDecimal calculatePoints(PointEarnStandardResponse standard, BigDecimal orderAmount) {
        if (standard.pointEarnType() == PointEarnType.FIXED) {
            return standard.point();
        } else if (standard.pointEarnType() == PointEarnType.PERCENTAGE) {
            BigDecimal percentage = standard.point()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return orderAmount.multiply(percentage).setScale(0, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }
}
