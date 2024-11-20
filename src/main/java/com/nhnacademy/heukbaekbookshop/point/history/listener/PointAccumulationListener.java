package com.nhnacademy.heukbaekbookshop.point.history.listener;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnType;
import com.nhnacademy.heukbaekbookshop.point.history.event.OrderEvent;
import com.nhnacademy.heukbaekbookshop.point.history.event.SignupEvent;
import com.nhnacademy.heukbaekbookshop.point.earn.dto.response.PointEarnStandardResponse;
import com.nhnacademy.heukbaekbookshop.point.earn.service.PointEarnStandardService;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import com.nhnacademy.heukbaekbookshop.point.history.dto.request.PointHistoryRequest;
import com.nhnacademy.heukbaekbookshop.point.history.service.PointSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PointAccumulationListener {
    private final PointEarnStandardService pointEarnStandardService;
    private final PointSaveService pointSaveService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSignupEvent(SignupEvent event) {
        processEvent(EventCode.SIGNUP, null, event.customerId(), BigDecimal.ZERO, PointType.EARNED);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        processEvent(EventCode.ORDER, event.orderId(), event.customerId(), event.orderAmount(), PointType.EARNED);
    }

    private void processEvent(EventCode eventCode, Long orderId, Long userId, BigDecimal orderAmount, PointType pointType) {
        List<PointEarnStandardResponse> standards = pointEarnStandardService.getValidStandardsByEvent(eventCode);

        for (PointEarnStandardResponse standard : standards) {
            try {
                BigDecimal points = calculatePoints(standard, orderAmount);
                PointHistoryRequest pointHistoryRequest = new PointHistoryRequest(
                        orderId,
                        eventCode.getDisplayName(),
                        points,
                        LocalDateTime.now(),
                        pointType
                );
                pointSaveService.createPointHistory(userId, pointHistoryRequest);
            } catch (Exception e) {
                // TODO Log
            }
        }
    }

    private BigDecimal calculatePoints(PointEarnStandardResponse standard, BigDecimal orderAmount) {
        if (standard.pointEarnType() == PointEarnType.FIXED) {
            return standard.point();
        } else if (standard.pointEarnType() == PointEarnType.PERCENTAGE) {
            BigDecimal percentage = standard.point();
            return orderAmount.multiply(percentage).setScale(0, RoundingMode.DOWN);
        }
        return BigDecimal.ZERO;
    }
}
