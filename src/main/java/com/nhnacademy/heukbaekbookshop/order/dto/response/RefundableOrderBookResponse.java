package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public record RefundableOrderBookResponse(
        Long bookId,
        String thumbnailUrl,
        String title,
        String price,
        int quantity,
        String salePrice,
        BigDecimal discountRate,
        String totalPrice
) implements Serializable {}
