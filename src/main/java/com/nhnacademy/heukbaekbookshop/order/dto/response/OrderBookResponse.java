package com.nhnacademy.heukbaekbookshop.order.dto.response;

import java.math.BigDecimal;

public record OrderBookResponse(String thumbnailUrl,
                                String title,
                                String price,
                                int quantity,
                                String salePrice,
                                BigDecimal discountRate,
                                String totalPrice) {
}
