package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long>, DeliveryFeeRepositoryCustom {
    Optional<DeliveryFee> findByName(String name);

    Optional<DeliveryFee> findByFee(BigDecimal fee);
}
