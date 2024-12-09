package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.DeliveryFee;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("deliveryFee-test.sql")
class DeliveryFeeRepositoryCustomTest {

    @Autowired
    private DeliveryFeeRepository deliveryFeeRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findByMinimumOrderAmount() {
        //given
        BigDecimal minimumOrderAmount1 = new BigDecimal(10000);
        BigDecimal minimumOrderAmount2 = new BigDecimal(50000);

        //when
        Optional<DeliveryFee> result1 = deliveryFeeRepository.findByMinimumOrderAmount(minimumOrderAmount1);
        Optional<DeliveryFee> result2 = deliveryFeeRepository.findByMinimumOrderAmount(minimumOrderAmount2);

        //then
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(BigDecimal.valueOf(5000).doubleValue(), result1.get().getFee().doubleValue());
        assertEquals(BigDecimal.ZERO.doubleValue(), result2.get().getFee().doubleValue());
    }
}