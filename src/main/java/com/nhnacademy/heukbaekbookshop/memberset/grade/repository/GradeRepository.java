package com.nhnacademy.heukbaekbookshop.memberset.grade.repository;

import com.nhnacademy.heukbaekbookshop.memberset.grade.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    boolean existsByGradeName(String gradeName);
}
