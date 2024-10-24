package com.nhnacademy.heukbaekbookshop.book.dto.request;

import java.util.Date;
import java.util.List;

public record BookUpdateRequest(
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
