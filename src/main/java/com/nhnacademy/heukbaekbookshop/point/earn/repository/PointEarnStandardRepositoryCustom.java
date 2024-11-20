package com.nhnacademy.heukbaekbookshop.point.earn.repository;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.EventCode;
import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnStandard;

import java.time.LocalDateTime;
import java.util.List;

public interface PointEarnStandardRepositoryCustom {
    List<PointEarnStandard> findValidStandardsByEventCode(EventCode eventCode, LocalDateTime now);
}
