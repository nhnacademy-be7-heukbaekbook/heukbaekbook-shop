package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BookSearchRequest(
        String keyword,
        String searchCondition,
        String sortCondition,
        Integer page,
        Integer size
) {
    public BookSearchRequest(String keyword) {
        this(keyword, "TITLE", "POPULARITY", 0, 10);
    }

    public BookSearchRequest(String keyword, String searchCondition, String sortCondition) {
        this(keyword, searchCondition, sortCondition, 0, 10);
    }
    @JsonCreator
    public BookSearchRequest(
            @JsonProperty("keyword") String keyword,
            @JsonProperty("searchCondition") String searchCondition,
            @JsonProperty("sortCondition") String sortCondition,
            @JsonProperty("page") Integer page,
            @JsonProperty("size") Integer size) {
        this.keyword = keyword != null ? keyword : "";
        this.searchCondition = searchCondition != null ? searchCondition : "TITLE";
        this.sortCondition = sortCondition != null ? sortCondition : "POPULARITY";
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 10;
    }
}

