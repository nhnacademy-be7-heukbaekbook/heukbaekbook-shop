package com.nhnacademy.heukbaekbookshop.point.earn.repository;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({PointEarnStandardRepositoryCustomImpl.class})
@Transactional
class PointEarnStandardRepositoryTest {

    @Autowired
    PointEarnStandardRepository pointEarnStandardRepository;

    @Autowired
    PointEarnEventRepository pointEarnEventRepository;

    PointEarnEvent testEvent;

    @BeforeEach
    void setup() {
        testEvent = new PointEarnEvent();
        testEvent.setEventCode(EventCode.LOGIN);
        testEvent.setPointEarnStandards(new HashSet<>());
        pointEarnEventRepository.save(testEvent);

        LocalDateTime now = LocalDateTime.now();

        PointEarnStandard validStandard1 = new PointEarnStandard(
                null,
                "Login Bonus",
                BigDecimal.valueOf(10),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                now.minusDays(1),
                now.plusDays(5),
                testEvent
        );

        PointEarnStandard expiredStandard = new PointEarnStandard(
                null,
                "Old Event",
                BigDecimal.valueOf(5),
                PointEarnType.FIXED,
                PointEarnStandardStatus.ACTIVATED,
                now.minusDays(10),
                now.minusDays(1),
                testEvent
        );

        PointEarnStandard deletedStandard = new PointEarnStandard(
                null,
                "Deleted Standard",
                BigDecimal.valueOf(20),
                PointEarnType.FIXED,
                PointEarnStandardStatus.DELETED,
                now.minusDays(1),
                now.plusDays(5),
                testEvent
        );

        PointEarnStandard noEndDateStandard = new PointEarnStandard(
                null,
                "Ongoing Event",
                BigDecimal.valueOf(15),
                PointEarnType.PERCENTAGE,
                PointEarnStandardStatus.ACTIVATED,
                now.minusDays(2),
                null,
                testEvent
        );

        pointEarnStandardRepository.saveAll(List.of(validStandard1, expiredStandard, deletedStandard, noEndDateStandard));
    }

    @Test
    void testFindValidStandardsByEventCode() {
        LocalDateTime now = LocalDateTime.now();

        List<PointEarnStandard> results = pointEarnStandardRepository.findValidStandardsByEventCode(EventCode.LOGIN, now);

        assertThat(results).hasSize(2);

        PointEarnStandard standard1 = results.get(0);
        PointEarnStandard standard2 = results.get(1);

        assertThat(List.of(standard1.getName(), standard2.getName())).contains("Login Bonus", "Ongoing Event");

        assertThat(standard1.getStatus()).isEqualTo(PointEarnStandardStatus.ACTIVATED);
        assertThat(standard1.getPointEarnEvent().getEventCode()).isEqualTo(EventCode.LOGIN);
        assertThat(standard1.getPointEarnStart()).isBeforeOrEqualTo(now);
        assertThat(standard1.getPointEarnEnd()).isAfter(now);

        assertThat(standard2.getStatus()).isEqualTo(PointEarnStandardStatus.ACTIVATED);
        assertThat(standard2.getPointEarnEvent().getEventCode()).isEqualTo(EventCode.LOGIN);
        assertThat(standard2.getPointEarnStart()).isBeforeOrEqualTo(now);
        assertThat(standard2.getPointEarnEnd()).isNull();
    }
}
