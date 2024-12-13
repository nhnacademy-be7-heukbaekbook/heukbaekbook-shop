package com.nhnacademy.heukbaekbookshop.common.sse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SseMessage {
    @JsonProperty
    private String message;

    private SseMessage(String message) {
        this.message = message;
    }

    public static SseMessage of(final String message) {
        return new SseMessage(message);
    }
}
