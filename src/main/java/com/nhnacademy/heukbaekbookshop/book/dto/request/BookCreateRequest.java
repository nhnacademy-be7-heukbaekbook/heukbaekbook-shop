package com.nhnacademy.heukbaekbookshop.book.dto.request;

import java.util.List;

public record BookCreateRequest(
        String title,
        String index,
        String description,
        List<String> categories,
        List<String> authors,
        String publisher,
        String pubDate,
        String isbn,
        int standardPrice,
        int salesPrice
) {}
