package com.nhnacademy.heukbaekbookshop.book.dto.request.book;

import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;

public record BookSearchRequest(
        String keyword,
        String searchCondition,
        String sortCondition,
        int page,
        int size
) {
    public BookSearchRequest(String keyword, String searchCondition, String sortCondition) {
        this(keyword, searchCondition, sortCondition, 0, 10); // 기본값 설정
    }
}