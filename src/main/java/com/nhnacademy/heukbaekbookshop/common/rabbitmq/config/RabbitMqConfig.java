package com.nhnacademy.heukbaekbookshop.common.rabbitmq.config;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.nhnacademy.heukbaekbookshop.common.rabbitmq.util.CouponRabbitMqConstants.*;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public DirectExchange couponIssueExchange() {
        return new DirectExchange(COUPON_ISSUE_EXCHANGE);
    }

    @Bean
    public Queue couponIssueRequestQueue(){
        return QueueBuilder.durable(COUPON_ISSUE_QUEUE)
                .withArgument("x-dead-letter-exchange", COUPON_ISSUE_DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", COUPON_ISSUE_DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding couponIssueRequestBinding() {
        return BindingBuilder.bind(couponIssueRequestQueue())
                .to(couponIssueExchange())
                .with(COUPON_ISSUE_ROUTING_KEY);
    }


    // CouponIssue DeadLetter

    @Bean
    public DirectExchange couponIssueDeadLetterExchange(){
        return new DirectExchange(COUPON_ISSUE_DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Queue couponIssueDeadLetterQueue(){
        return QueueBuilder.durable(COUPON_ISSUE_DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding couponIssueDeadLetterBinding() {
        return BindingBuilder.bind(couponIssueDeadLetterQueue())
                .to(couponIssueDeadLetterExchange())
                .with(COUPON_ISSUE_DEAD_LETTER_ROUTING_KEY);
    }

}
