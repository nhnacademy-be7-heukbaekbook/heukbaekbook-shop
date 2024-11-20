package com.nhnacademy.heukbaekbookshop.book.exception.book;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("Book not found: " + bookId);
    }
}
