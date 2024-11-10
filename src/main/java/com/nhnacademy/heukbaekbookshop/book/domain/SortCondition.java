package com.nhnacademy.heukbaekbookshop.book.domain;

import lombok.Getter;

@Getter
public enum SortCondition {
    POPULARITY("book_popularity"),
    NEWEST("book_publication"),
    LOWEST_PRICE("book_price"),
    HIGHEST_PRICE("book_price"),
    RATING("book_discount_rate"),
    REVIEW_COUNT(""), // 추가시켜야함
    NONE("");

    private final String fieldName;

    SortCondition(String fieldName) {
        this.fieldName = fieldName;
    }
}

