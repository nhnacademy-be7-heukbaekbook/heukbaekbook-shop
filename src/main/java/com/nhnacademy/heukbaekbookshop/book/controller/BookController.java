package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookCreateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.request.book.BookUpdateRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.*;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookService;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final LikeService likeService;

    @Autowired
    public BookController(BookService bookService, LikeService likeService) {
        this.bookService = bookService;
        this.likeService = likeService;
    }

    @GetMapping("/aladin")
    public ResponseEntity<List<BookSearchResponse>> searchBooks(@RequestParam("title") String title) {
        List<BookSearchResponse> bookSearchResponses = bookService.searchBook(title);
        return ResponseEntity.ok(bookSearchResponses);
    }

    @PostMapping("/admins")
    public ResponseEntity<BookCreateResponse> createBook(@RequestBody BookCreateRequest request) {
        BookCreateResponse response = bookService.registerBook(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admins/{bookId}")
    public ResponseEntity<BookUpdateResponse> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookUpdateRequest request
    ) {
        BookUpdateResponse response = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admins/{bookId}")
    public ResponseEntity<BookDeleteResponse> deleteBook(@PathVariable Long bookId) {
        BookDeleteResponse response = bookService.deleteBook(bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable Long bookId) {
        BookDetailResponse response = bookService.getBook(bookId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{bookId}/likes")
    public ResponseEntity<LikeCreateResponse> createLike(
            @PathVariable Long bookId,
            @RequestParam Long customerId) {
        LikeCreateResponse response = likeService.createLike(bookId, customerId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookId}/likes")
    public ResponseEntity<LikeDeleteResponse> deleteLike(
            @PathVariable Long bookId,
            @RequestParam Long customerId) {
        LikeDeleteResponse response = likeService.deleteLike(bookId, customerId);
        return ResponseEntity.ok(response);
    }

}

