package com.nhnacademy.heukbaekbookshop.point.earn.repository;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.heukbaekbookshop.point.earn.domain.QPointEarnStandard.pointEarnStandard;

@Repository
@RequiredArgsConstructor
public class PointEarnStandardRepositoryCustomImpl implements PointEarnStandardRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<PointEarnStandard> findValidStandardsByEventCode(EventCode eventCode, LocalDateTime now) {
        return factory.selectFrom(pointEarnStandard)
                .where(
                        pointEarnStandard.pointEarnEvent.eventCode.eq(eventCode),
                        pointEarnStandard.status.eq(PointEarnStandardStatus.ACTIVATED),
                        pointEarnStandard.pointEarnStart.loe(now),
                        pointEarnStandard.pointEarnEnd.isNull().or(pointEarnStandard.pointEarnEnd.goe(now))
                )
                .fetch();
    }
}
