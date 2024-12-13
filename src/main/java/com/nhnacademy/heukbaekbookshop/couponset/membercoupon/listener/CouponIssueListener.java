package com.nhnacademy.heukbaekbookshop.couponset.membercoupon.listener;

import com.nhnacademy.heukbaekbookshop.common.sse.RedisPublisher;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.dto.request.CouponIssueRequest;
import com.nhnacademy.heukbaekbookshop.couponset.membercoupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueListener {
    private static final String TOPIC_PREFIX = "sse:publish:customerId:%d";

    private final CouponIssueService couponIssueService;
//    private final RedisPublisher redisPublisher;

    @Transactional
    @RabbitListener(queues = "heukbaekbook.coupon.queue", concurrency = "1")
    public void receiveMessage(CouponIssueRequest issueMessage) {
        if(isValidMessage(issueMessage)) {
            log.debug("Received RabbitMQ Message is not valid");
            throw new AmqpException("Invalid message received");
        }

        couponIssueService.issueCouponAsync(issueMessage);

//        String topic = TOPIC_PREFIX.formatted(issueMessage.customerId());
//        redisPublisher.publish(topic, SseMessage.of("쿠폰이 정상 발급되었습니다."));
    }


    boolean isValidMessage(CouponIssueRequest issueMessage) {
        return Objects.isNull(issueMessage.couponId())
                || Objects.isNull(issueMessage.couponId())
                || Objects.isNull(issueMessage.couponExpirationDate());
    }
}
