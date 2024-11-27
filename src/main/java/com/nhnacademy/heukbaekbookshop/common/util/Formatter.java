package com.nhnacademy.heukbaekbookshop.common.util;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class Formatter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");

    public static String formatPrice(BigDecimal price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(price);
    }

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
