package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long>, WrappingPaperRepositoryCustom {
//    List<WrappingPaper> findByIsAvailable(boolean isAvailable);
}