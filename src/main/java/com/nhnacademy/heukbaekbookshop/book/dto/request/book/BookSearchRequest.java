package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BookSearchRequest(
        String keyword,
        String searchCondition,
        String sortCondition,
        Long categoryId
) {
    @JsonCreator
    public BookSearchRequest(
            @JsonProperty("keyword") String keyword,
            @JsonProperty("searchCondition") String searchCondition,
            @JsonProperty("sortCondition") String sortCondition,
            @JsonProperty("categoryId") Long categoryId
            ) {
        this.keyword = keyword != null ? keyword : "";
        this.searchCondition = searchCondition != null ? searchCondition : "ALL";
        this.sortCondition = sortCondition != null ? sortCondition : "POPULARITY";
        this.categoryId = categoryId;
    }
}

