package com.nhnacademy.heukbaekbookshop.book.dto.request;

import java.util.List;

public record BookCreateRequest(
        String title,
        String index,
        String description,
        String publication,
        String isbn,
        boolean isPackable,
        int stock,
        int standardPrice,
        float discountRate,
        String publisher,
        List<String> categories,
        List<String> authors
) {}
