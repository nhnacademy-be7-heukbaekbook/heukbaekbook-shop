package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import java.util.Date;
import java.util.List;

public record BookResponse(
    Long id,
    String title,
    String index,
    String description,
    List<String> categories,
    List<String> authors,
    String publisher,
    Date pubDate,
    String isbn,
    int standardPrice,
    int salesPrice
) {}
