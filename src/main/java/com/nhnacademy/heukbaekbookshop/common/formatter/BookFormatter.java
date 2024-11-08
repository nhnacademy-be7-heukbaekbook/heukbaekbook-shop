package com.nhnacademy.heukbaekbookshop.common.formatter;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class BookFormatter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");

    public String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(price);
    }

    public String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
