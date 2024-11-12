package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.*;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class BookAdminController {

    private final BookService bookService;

    public BookAdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/aladin")
    public ResponseEntity<List<BookSearchResponse>> searchBooks(@RequestParam("title") String title) {
        List<BookSearchResponse> bookSearchResponses = bookService.searchBook(title);
        return ResponseEntity.ok(bookSearchResponses);
    }

    @PostMapping("/books")
    public ResponseEntity<BookCreateResponse> registerBook(@RequestBody BookCreateRequest request) {
        BookCreateResponse response = bookService.registerBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<BookUpdateResponse> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookUpdateRequest request
    ) {
        BookUpdateResponse response = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<BookDeleteResponse> deleteBook(@PathVariable Long bookId) {
        BookDeleteResponse response = bookService.deleteBook(bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books")
    public ResponseEntity<Page<BookDetailResponse>> getBooks(Pageable pageable) {
        Page<BookDetailResponse> response = bookService.getBooksDetail(pageable);
        return ResponseEntity.ok(response);
    }
}
