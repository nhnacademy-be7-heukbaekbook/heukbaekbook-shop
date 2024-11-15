package com.nhnacademy.heukbaekbookshop.point.repository;

import com.nhnacademy.heukbaekbookshop.point.domain.earn.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.domain.earn.PointEarnStandardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointEarnStandardRepository extends JpaRepository<PointEarnStandard, Long> {
    List<PointEarnStandard> findAllByStatusNot(PointEarnStandardStatus status);
}
