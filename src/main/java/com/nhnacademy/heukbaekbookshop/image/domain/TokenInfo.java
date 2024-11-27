package com.nhnacademy.heukbaekbookshop.image.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenInfo {
    private String id;
    private LocalDateTime expires;
}
