package com.nhnacademy.heukbaekbookshop.book.exception.book;

import org.springframework.web.client.RestClientException;

public class BookSearchException extends RuntimeException {
    public BookSearchException(String message, RestClientException e) {
        super(message, e);
    }
}
