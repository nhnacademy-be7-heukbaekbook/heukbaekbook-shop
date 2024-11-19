package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BookSearchRequest(
        String keyword,
        String searchCondition,
        String sortCondition
) {
    @JsonCreator
    public BookSearchRequest(
            @JsonProperty("keyword") String keyword,
            @JsonProperty("searchCondition") String searchCondition,
            @JsonProperty("sortCondition") String sortCondition
            ) {
        this.keyword = keyword != null ? keyword : "";
        this.searchCondition = searchCondition != null ? searchCondition : "ALL";
        this.sortCondition = sortCondition != null ? sortCondition : "POPULARITY";
    }
}

