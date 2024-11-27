package com.nhnacademy.heukbaekbookshop.common.util;

import java.math.BigDecimal;

public abstract class Converter {

    public static BigDecimal convertStringToBigDecimal(String amount) {
        amount = amount.replace(",", "");
        return new BigDecimal(amount);
    }
}
