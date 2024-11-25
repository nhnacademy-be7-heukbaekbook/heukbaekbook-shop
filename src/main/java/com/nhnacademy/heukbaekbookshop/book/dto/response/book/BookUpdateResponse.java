package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import java.math.BigDecimal;
import java.util.List;

public record BookUpdateResponse(
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
        String bookStatus,
        String publisher,
        List<String> categories,
        List<String> authors
) {}
