package com.nhnacademy.heukbaekbookshop.member.repository;

import com.nhnacademy.heukbaekbookshop.member.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
