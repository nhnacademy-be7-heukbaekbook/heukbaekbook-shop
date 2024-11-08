package com.nhnacademy.heukbaekbookshop.book.exception.like;

public class LikeAlreadyExistException extends RuntimeException {
    public LikeAlreadyExistException(String message) {
        super(message);
    }
}
