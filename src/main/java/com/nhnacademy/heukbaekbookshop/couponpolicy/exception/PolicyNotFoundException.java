package com.nhnacademy.heukbaekbookshop.couponpolicy.exception;

public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException(String message) {
        super(message);
    }
}
