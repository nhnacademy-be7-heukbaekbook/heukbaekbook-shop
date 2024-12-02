package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;

import java.math.BigDecimal;
import java.util.Optional;

public interface DeliveryFeeRepositoryCustom {

    Optional<DeliveryFee> findByMinimumOrderAmount(BigDecimal minimumOrderAmount);
}
