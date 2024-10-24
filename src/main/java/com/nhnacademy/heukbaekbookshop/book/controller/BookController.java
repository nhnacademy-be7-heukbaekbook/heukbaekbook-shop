package com.nhnacademy.heukbaekbookshop.book.controller;

import com.nhnacademy.heukbaekbookshop.book.dto.BookSearchRequest;
import com.nhnacademy.heukbaekbookshop.book.dto.BookSearchResponse;
import com.nhnacademy.heukbaekbookshop.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("bookSearchRequest", new BookSearchRequest("empty"));
        return "books";
    }

    @PostMapping("/books")
    public String searchBooks(@ModelAttribute BookSearchRequest bookSearchRequest, Model model) {
        List<BookSearchResponse> bookSearchResponses = bookService.searchBook(bookSearchRequest);
        model.addAttribute("responses", bookSearchResponses);
        return "books";
    }
}
