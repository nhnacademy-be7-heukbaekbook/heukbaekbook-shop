package com.nhnacademy.heukbaekbookshop.book.domain;

import lombok.Getter;

@Getter
public enum SearchCondition {
    TITLE("book_title"),
    AUTHOR("book_author"),
    DESCRIPTION("book_description"),
    NONE("");

    private final String field;

    SearchCondition(String field) {
        this.field = field;
    }
}

