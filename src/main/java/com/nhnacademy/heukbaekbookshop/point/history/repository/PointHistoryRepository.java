package com.nhnacademy.heukbaekbookshop.point.history.repository;

import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findByMemberIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);
}
