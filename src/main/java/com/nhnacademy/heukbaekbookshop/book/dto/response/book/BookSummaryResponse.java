package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import java.math.BigDecimal;

public record BookSummaryResponse(Long id,
                                  String title,
                                  boolean isPackable,
                                  BigDecimal price,
                                  BigDecimal salePrice,
                                  BigDecimal discountRate,
                                  String thumbnailUrl) {
}
