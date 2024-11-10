package com.nhnacademy.heukbaekbookshop.common.advice;

import java.time.ZonedDateTime;

public record ErrorResponse(String title, int status, ZonedDateTime timestamp) {

}
