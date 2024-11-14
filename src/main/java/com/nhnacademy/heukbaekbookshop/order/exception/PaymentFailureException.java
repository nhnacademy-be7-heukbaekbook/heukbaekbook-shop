package com.nhnacademy.heukbaekbookshop.order.exception;

public class PaymentFailureException extends RuntimeException {
    public PaymentFailureException(String message) {
        super(message);
    }
}
