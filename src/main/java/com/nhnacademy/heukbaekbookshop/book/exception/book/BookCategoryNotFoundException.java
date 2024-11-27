package com.nhnacademy.heukbaekbookshop.book.exception.book;

public class BookCategoryNotFoundException extends RuntimeException {
    public BookCategoryNotFoundException(String message) {
        super(message);
    }
}
