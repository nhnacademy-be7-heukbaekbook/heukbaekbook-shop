package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponStatus;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.enums.CouponType;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.dto.CouponPolicyResponse;
import com.nhnacademy.heukbaekbookshop.couponset.couponpolicy.domain.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponRedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    private CouponRedisService couponRedisService;

    @BeforeEach
    void setUp() {
        couponRedisService = new CouponRedisService(redisTemplate);
    }

    @Test
    void saveCouponToRedis() {
        when(redisTemplate.<String, Object>opsForHash()).thenReturn(hashOperations);

        CouponPolicyResponse couponPolicyResponse = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(15.0),
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(1000.0)
        );

        LocalDateTime now = LocalDateTime.now();
        CouponResponse couponResponse = new CouponResponse(
                1L,
                couponPolicyResponse,
                CouponStatus.ABLE,
                10,
                30,
                now,
                now.plusDays(1),
                "Test",
                "test",
                now,
                CouponType.GENERAL
        );

        String key = "coupon:1";
        Map<String, Object> couponData = Map.of(
                "couponId", couponResponse.couponId(),
                "couponQuantity", couponResponse.couponQuantity(),
                "availableDuration", couponResponse.availableDuration(),
                "couponTimeStart", couponResponse.couponTimeStart().toString(),
                "couponTimeEnd", couponResponse.couponTimeEnd().toString()
        );

        couponRedisService.saveCouponToRedis(couponResponse);

        verify(hashOperations).putAll(key, couponData);
    }

    @Test
    void deleteCouponFromRedis() {
        Long couponId = 1L;
        String key = "coupon:" + couponId;

        couponRedisService.deleteCouponFromRedis(couponId);

        verify(redisTemplate).delete(key);
    }
}
