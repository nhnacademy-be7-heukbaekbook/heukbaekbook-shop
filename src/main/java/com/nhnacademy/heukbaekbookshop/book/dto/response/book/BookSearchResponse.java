package com.nhnacademy.heukbaekbookshop.book.dto.response.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class BookSearchResponse {
    private String title;
    private String cover;
    private String description;
    private String category;
    private String author;
    private String publisher;
    private LocalDate pubDate;
    private String isbn;
    private int standardPrice;
    private int salesPrice;
}
