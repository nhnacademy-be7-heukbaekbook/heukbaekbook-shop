package com.nhnacademy.heukbaekbookshop.point.earn.repository;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.heukbaekbookshop.point.earn.domain.QPointEarnStandard.pointEarnStandard;

@Repository
public class PointEarnStandardRepositoryCustomImpl implements PointEarnStandardRepositoryCustom {

    private final JPAQueryFactory factory;

    public PointEarnStandardRepositoryCustomImpl(EntityManager em) {
        this.factory = new JPAQueryFactory(em);
    }

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
