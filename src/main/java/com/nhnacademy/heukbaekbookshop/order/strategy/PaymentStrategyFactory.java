package com.nhnacademy.heukbaekbookshop.order.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentStrategyFactory {
    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyFactory(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getPaymentMethodName().toUpperCase(),
                        strategy -> strategy
                ));
    }

    public PaymentStrategy getStrategy(String method) {
        PaymentStrategy strategy = strategies.get(method.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 결제 방법입니다: " + method);
        }
        return strategy;
    }
}
