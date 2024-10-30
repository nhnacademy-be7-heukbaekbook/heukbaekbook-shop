package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import java.util.List;

public record BookUpdateRequest(
        String title,
        String index,
        String description,
        String publication,
        String isbn,
        List<String> imageUrls,
        boolean isPackable,
        int stock,
        int standardPrice,
        float discountRate,
        String bookStatus,
        String publisher,
        List<String> categories,
        String authors,
        List<String> tags
) {}
