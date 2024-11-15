package com.nhnacademy.heukbaekbookshop.point.earn.repository;

import com.nhnacademy.heukbaekbookshop.point.earn.domain.PointEarnEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointEarnEventRepository extends JpaRepository<PointEarnEvent, Long> {
}
