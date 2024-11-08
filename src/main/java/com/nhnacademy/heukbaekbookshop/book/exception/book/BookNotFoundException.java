package com.nhnacademy.heukbaekbookshop.book.exception.book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("Book not found: " + bookId);
    }
}
