package com.nhnacademy.heukbaekbookshop.memberset.member.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long customerId) {
        super(customerId + " member not found");
    }
}
