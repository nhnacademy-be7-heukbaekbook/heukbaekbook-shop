package com.nhnacademy.heukbaekbookshop.memberset.member.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {super(message);}
}
