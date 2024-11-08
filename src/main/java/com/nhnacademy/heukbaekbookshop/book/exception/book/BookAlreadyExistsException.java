package com.nhnacademy.heukbaekbookshop.book.exception.book;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("Book already exists: " + isbn);
    }
}
