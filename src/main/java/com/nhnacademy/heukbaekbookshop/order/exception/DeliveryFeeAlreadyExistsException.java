package com.nhnacademy.heukbaekbookshop.order.exception;

public class DeliveryFeeAlreadyExistsException extends RuntimeException {
    public DeliveryFeeAlreadyExistsException(String message) {
        super(message);
    }
}
