package com.nhnacademy.heukbaekbookshop.point.history.repository;

import com.nhnacademy.heukbaekbookshop.point.history.domain.PointHistory;
import com.nhnacademy.heukbaekbookshop.point.history.domain.PointType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Page<PointHistory> findByMemberId(Long customerId, Pageable pageable);

    Optional<PointHistory> findFirstByMemberIdOrderByCreatedAtDesc(Long customerId);

    boolean existsByMemberIdAndOrderId(Long customerId, Long orderId);

}
