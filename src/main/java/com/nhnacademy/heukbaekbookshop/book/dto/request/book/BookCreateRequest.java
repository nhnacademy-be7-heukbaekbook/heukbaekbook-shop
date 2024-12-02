package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import java.math.BigDecimal;

public record BookCreateRequest(
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        String imageUrl,
        boolean isPackable,
        int stock,
        int standardPrice,
        BigDecimal discountRate,
        String publisher,
        String categories,
        String authors
) {}
