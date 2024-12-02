package com.nhnacademy.heukbaekbookshop.book.domain;

import lombok.Getter;

@Getter
public enum SortCondition {
    POPULARITY("book_popularity"),
    NEWEST("book_publication"),
    LOWEST_PRICE("book_price"),
    HIGHEST_PRICE("book_price"),
    RATING("book_rating"),
    REVIEW_COUNT("book_review_count"); // 추가시켜야함

    private final String fieldName;

    SortCondition(String fieldName) {
        this.fieldName = fieldName;
    }
}

