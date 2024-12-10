package com.nhnacademy.heukbaekbookshop.order.strategy;

import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategyFactory;
import com.nhnacademy.heukbaekbookshop.order.strategy.PaymentStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PaymentStrategyFactoryTest.TestConfig.class)
class PaymentStrategyFactoryTest {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;

    @Autowired
    private PaymentStrategy tossPaymentStrategy;

    @Autowired
    private PaymentStrategy payPalPaymentStrategy;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentStrategyFactory paymentStrategyFactory(List<PaymentStrategy> strategies) {
            return new PaymentStrategyFactory(strategies);
        }

        @Bean
        public PaymentStrategy tossPaymentStrategy() {
            PaymentStrategy mock = mock(PaymentStrategy.class);
            when(mock.getPaymentMethodName()).thenReturn("TOSS");
            return mock;
        }

        @Bean
        public PaymentStrategy payPalPaymentStrategy() {
            PaymentStrategy mock = mock(PaymentStrategy.class);
            when(mock.getPaymentMethodName()).thenReturn("PAYPAL");
            return mock;
        }
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("toss.secret-key", () -> "testSecretKey");
        registry.add("paypal.secret-key", () -> "testPayPalSecretKey");
    }

    @Test
    void testGetStrategy_Toss() {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy("TOSS");
        assertNotNull(strategy, "TOSS 전략을 반환해야 합니다.");
        assertEquals(tossPaymentStrategy, strategy, "TOSS 전략이 일치해야 합니다.");
    }

    @Test
    void testGetStrategy_PayPal() {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy("PAYPAL");
        assertNotNull(strategy, "PAYPAL 전략을 반환해야 합니다.");
        assertEquals(payPalPaymentStrategy, strategy, "PAYPAL 전략이 일치해야 합니다.");
    }

    @Test
    void testGetStrategy_CaseInsensitive() {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy("tOsS");
        assertNotNull(strategy, "대소문자 구분 없이 TOSS 전략을 반환해야 합니다.");
        assertEquals(tossPaymentStrategy, strategy, "TOSS 전략이 일치해야 합니다.");
    }

    @Test
    void testGetStrategy_InvalidMethod() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentStrategyFactory.getStrategy("INVALID");
        }, "지원하지 않는 결제 방법에 대해 예외가 발생해야 합니다.");

        assertTrue(exception.getMessage().contains("지원하지 않는 결제 방법입니다: INVALID"),
                "예외 메시지가 올바르게 설정되어야 합니다.");
    }

    @Test
    void testGetStrategy_EmptyMethod() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentStrategyFactory.getStrategy("");
        }, "빈 문자열 결제 방법에 대해 예외가 발생해야 합니다.");

        assertTrue(exception.getMessage().contains("지원하지 않는 결제 방법입니다: "),
                "예외 메시지가 올바르게 설정되어야 합니다.");
    }
}