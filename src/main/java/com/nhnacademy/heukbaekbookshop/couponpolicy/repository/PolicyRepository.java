package com.nhnacademy.heukbaekbookshop.couponpolicy.repository;

import com.nhnacademy.heukbaekbookshop.couponpolicy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
