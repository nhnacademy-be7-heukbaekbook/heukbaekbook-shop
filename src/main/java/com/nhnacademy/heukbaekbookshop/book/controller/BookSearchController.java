package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
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

    @GetMapping("/search")
    public Page<BookResponse> searchBooks(@RequestParam("keyword") String keyword,
                                          @RequestParam("searchCondition") String searchCondition,
                                          @RequestParam(value = "sortCondition", required = false) String sortCondition) {

        if (sortCondition == null || sortCondition.isEmpty()) {
            sortCondition = "POPULARITY";
        }

        PageRequest pageRequest = PageRequest.of(0, 10); // 기본 페이지 설정
        BookSearchRequest searchRequest = new BookSearchRequest(keyword, searchCondition, sortCondition);
        return bookSearchService.searchBooks(pageRequest, searchRequest);
    }
}
