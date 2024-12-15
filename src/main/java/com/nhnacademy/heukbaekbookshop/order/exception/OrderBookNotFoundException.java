package com.nhnacademy.heukbaekbookshop.order.exception;

public class OrderBookNotFoundException extends RuntimeException {
    public OrderBookNotFoundException(String message) {
        super(message);
    }
}
