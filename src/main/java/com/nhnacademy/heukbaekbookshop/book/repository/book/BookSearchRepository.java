package com.nhnacademy.heukbaekbookshop.book.repository.book;


import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookSearchRepository {
    Page<BookDocument> search(String indexName, Pageable pageable, String keyword, SearchCondition condition, SortCondition sortCondition, Long categoryId);
}