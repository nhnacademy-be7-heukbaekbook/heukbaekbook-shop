package com.nhnacademy.heukbaekbookshop.point.earn.repository;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandard;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointEarnStandardRepository extends JpaRepository<PointEarnStandard, Long>, PointEarnStandardRepositoryCustom {
    List<PointEarnStandard> findAllByStatusNot(PointEarnStandardStatus status);
}
