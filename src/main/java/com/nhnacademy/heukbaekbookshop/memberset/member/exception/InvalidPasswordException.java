package com.nhnacademy.heukbaekbookshop.memberset.member.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
