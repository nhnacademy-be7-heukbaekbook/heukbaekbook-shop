package com.nhnacademy.heukbaekbookshop.book.domain;

import lombok.Getter;

@Getter
public enum SearchCondition {
    ALL(""),
    TITLE("book_title"),
    AUTHOR("book_author"),
    DESCRIPTION("book_description");

    private final String field;

    SearchCondition(String field) {
        this.field = field;
    }
}

