package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.service.book.BookCategoryService;
import com.nhnacademy.heukbaekbookshop.category.dto.response.ParentCategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;

    @GetMapping("/books/{bookId}/book-categories")
    public List<ParentCategoryResponse> getBookCategoriesByBookId(@PathVariable Long bookId) {
        log.info("bookId: {}", bookId);
        return bookCategoryService.findBookCategoriesByBookId(bookId);
    }
}
