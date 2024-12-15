package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookDetailResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.book.BookViewResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeCreateResponse;
import com.nhnacademy.heukbaekbookshop.book.dto.response.like.LikeDeleteResponse;
import com.nhnacademy.heukbaekbookshop.book.service.book.BookService;
import com.nhnacademy.heukbaekbookshop.book.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final LikeService likeService;

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


    @GetMapping("/summary")
    public ResponseEntity<List<BookSummaryResponse>> getBooksSummary(@RequestParam List<Long> bookIds) {
        List<BookSummaryResponse> bookSummaryResponses = bookService.getBooksSummary(bookIds);
        return ResponseEntity.ok(bookSummaryResponses);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getBooks(Pageable pageable) {
        log.info("pageable: {}", pageable);
        Page<BookResponse> books = bookService.getBooks(pageable);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Page<BookResponse>> getBooksByCategoryId(@PathVariable Long categoryId,
                                                                   Pageable pageable) {
        log.info("categoryId: {}", categoryId);
        Page<BookResponse> books = bookService.getBooksByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/detail")
    public BookViewResponse getBookDetail(@RequestParam Long bookId) {
        log.info("bookId: {}", bookId);
        bookService.increasePopularity(bookId);
        return bookService.getBookDetail(bookId);
    }
}