package com.nhnacademy.heukbaekbookshop.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Calculator {

    public static BigDecimal getSalePrice(BigDecimal price, BigDecimal disCountRate) {
        BigDecimal discountRate = disCountRate.divide(BigDecimal.valueOf(100));
        return price.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }
}
