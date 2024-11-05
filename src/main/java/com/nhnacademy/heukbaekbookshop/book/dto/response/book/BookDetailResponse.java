package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import java.util.List;

public record BookDetailResponse(
        Long id,
        String title,
        String index,
        String description,
        String publication,
        String isbn,
        String imageUrl,
        boolean isPackable,
        int stock,
        int standardPrice,
        float discountRate,
        String bookStatus,
        String publisher,
        List<String> categories,
        List<String> authors,
        List<String> tags
) {}
