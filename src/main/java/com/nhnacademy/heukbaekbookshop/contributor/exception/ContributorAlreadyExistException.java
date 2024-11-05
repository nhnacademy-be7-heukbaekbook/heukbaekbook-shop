package com.nhnacademy.heukbaekbookshop.contributor.exception;

public class ContributorAlreadyExistException extends RuntimeException {
    public ContributorAlreadyExistException(String message) {
        super(message);
    }
}
