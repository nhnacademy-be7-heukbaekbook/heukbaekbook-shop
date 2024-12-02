package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import java.math.BigDecimal;
import java.util.List;

public record BookDetailResponse(
        Long id,
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        String thumbnailImageUrl,
        List<String> detailImageUrls,
        boolean isPackable,
        int stock,
        int standardPrice,
        BigDecimal discountRate,
        String bookStatus,
        String publisher,
        List<String> categories,
        List<String> authors,
        List<String> tags
) {}
