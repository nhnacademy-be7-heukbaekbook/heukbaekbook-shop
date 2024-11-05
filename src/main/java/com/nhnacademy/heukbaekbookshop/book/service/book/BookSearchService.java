package com.nhnacademy.heukbaekbookshop.book.service.book;


import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookSearchService {
    Page<BookSearchResponse> searchBooks(Pageable pageable, BookSearchRequest searchRequest);
}



