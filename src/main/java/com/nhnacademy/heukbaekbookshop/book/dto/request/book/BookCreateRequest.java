package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

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
        float discountRate,
        String publisher,
        String categories,
        String authors
) {}
