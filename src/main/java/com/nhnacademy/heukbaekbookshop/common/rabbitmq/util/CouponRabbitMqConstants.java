package com.nhnacademy.heukbaekbookshop.common.rabbitmq.util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponRabbitMqConstants {
    public static final String COUPON_ISSUE_EXCHANGE = "heukbaekbook.coupon.exchange";
    public static final String COUPON_ISSUE_ROUTING_KEY = "heukbaekbook.coupon.key";
    public static final String COUPON_ISSUE_QUEUE = "heukbaekbook.coupon.queue";

    public static final String COUPON_ISSUE_DEAD_LETTER_EXCHANGE = "heukbaekbook.coupon.deadletter.exchange";
    public static final String COUPON_ISSUE_DEAD_LETTER_ROUTING_KEY = "heukbaekbook.coupon.deadletter.key";
    public static final String COUPON_ISSUE_DEAD_LETTER_QUEUE = "heukbaekbook.coupon.deadletter.queue";
}
