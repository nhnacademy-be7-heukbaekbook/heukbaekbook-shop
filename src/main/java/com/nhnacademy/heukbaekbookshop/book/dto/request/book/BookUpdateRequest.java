package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import java.math.BigDecimal;
import java.util.List;

public record BookUpdateRequest(
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
        String authors,
        List<String> tags
) {}
