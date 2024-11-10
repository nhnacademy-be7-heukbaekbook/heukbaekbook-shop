package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookElasticSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @PostMapping("/search")
    public Page<BookElasticSearchResponse> searchBooks(@RequestBody BookSearchRequest searchRequest) {
        PageRequest pageRequest = PageRequest.of(searchRequest.page(), searchRequest.size());
        return bookSearchService.searchBooks(pageRequest, searchRequest);
    }
}
