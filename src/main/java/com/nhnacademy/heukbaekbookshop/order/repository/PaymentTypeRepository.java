package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
}
