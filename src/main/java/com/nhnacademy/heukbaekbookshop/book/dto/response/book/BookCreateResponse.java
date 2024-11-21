package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import java.util.List;

public record BookCreateResponse(
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        boolean isPackable,
        int stock,
        int standardPrice,
        float discountRate,
        String publisher,
        List<String> categories,
        List<String> authors
) {}
