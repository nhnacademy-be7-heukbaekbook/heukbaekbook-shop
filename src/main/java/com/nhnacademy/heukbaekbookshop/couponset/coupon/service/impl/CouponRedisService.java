package com.nhnacademy.heukbaekbookshop.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekbookshop.couponset.coupon.dto.response.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "coupon:";

    public void saveCouponToRedis(CouponResponse couponResponse) {
        String key = KEY_PREFIX + couponResponse.couponId();

        Map<String, Object> couponData = new HashMap<>();
        couponData.put("couponId", couponResponse.couponId());
        couponData.put("couponQuantity", couponResponse.couponQuantity());
        couponData.put("availableDuration", couponResponse.availableDuration());
        couponData.put("couponTimeStart", couponResponse.couponTimeStart().toString());
        couponData.put("couponTimeEnd", couponResponse.couponTimeEnd().toString());

        redisTemplate.opsForHash().putAll(key, couponData);
    }

    public void deleteCouponFromRedis(Long couponId) {
        String key = KEY_PREFIX + couponId;
        redisTemplate.delete(key);
    }

}
