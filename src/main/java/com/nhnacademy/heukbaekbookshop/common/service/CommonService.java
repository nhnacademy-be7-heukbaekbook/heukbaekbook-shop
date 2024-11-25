package com.nhnacademy.heukbaekbookshop.common.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class CommonService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");

    public BigDecimal getSalePrice(BigDecimal price, BigDecimal disCountRate) {
        BigDecimal discountRate = disCountRate.divide(BigDecimal.valueOf(100));
        return price.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }

    public String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(price);
    }

    public String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public BigDecimal convertStringToBigDecimal(String amount) {
        amount = amount.replace(",", "");
        return new BigDecimal(amount);
    }
}
