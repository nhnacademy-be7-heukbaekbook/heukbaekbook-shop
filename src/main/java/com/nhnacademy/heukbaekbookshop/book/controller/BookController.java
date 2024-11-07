package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookService;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;
    private final LikeService likeService;

    public BookController(BookService bookService, LikeService likeService) {
        this.bookService = bookService;
        this.likeService = likeService;
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable Long bookId) {
        BookDetailResponse response = bookService.getBook(bookId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/books/{bookId}/likes")
    public ResponseEntity<LikeCreateResponse> createLike(
            @PathVariable Long bookId,
            @RequestParam Long customerId) {
        LikeCreateResponse response = likeService.createLike(bookId, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/books/{bookId}/likes")
    public ResponseEntity<LikeDeleteResponse> deleteLike(
            @PathVariable Long bookId,
            @RequestParam Long customerId) {
        LikeDeleteResponse response = likeService.deleteLike(bookId, customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/summary")
    public ResponseEntity<List<BookSummaryResponse>> getBooksSummary(@RequestParam List<Long> bookIds) {
        List<BookSummaryResponse> booksSummary = bookService.getBooksSummary(bookIds);
        return ResponseEntity.ok(booksSummary);
    }

}
