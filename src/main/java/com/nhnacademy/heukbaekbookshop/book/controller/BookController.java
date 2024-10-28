package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.request.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.*;
import com.nhnacademy.heukbaekbookshop.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/aladin")
    public ResponseEntity<List<BookSearchResponse>> searchBooks(@ModelAttribute BookSearchRequest bookSearchRequest, Model model) {
        List<BookSearchResponse> bookSearchResponses = bookService.searchBook(bookSearchRequest);
        return ResponseEntity.ok(bookSearchResponses);
    }

    @PostMapping
    public ResponseEntity<BookCreateResponse> createBook(@RequestBody BookCreateRequest request) {
        BookCreateResponse response = bookService.registerBook(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookUpdateResponse> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookUpdateRequest request
    ) {
        BookUpdateResponse response = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<BookDeleteResponse> deleteBook(@PathVariable Long bookId) {
        BookDeleteResponse response = bookService.deleteBook(bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long bookId) {
        BookResponse response = bookService.getBook(bookId);
        return ResponseEntity.ok(response);
    }

}

