package com.nhnacademy.heukbaekbookshop.point.earn.domain;

import lombok.Getter;

@Getter
public enum EventCode {
    LOGIN("로그인"),
    SIGNUP("회원가입"),
    ORDER("주문"),
    REVIEW_WITH_PHOTO("포토 리뷰"),
    REVIEW_WITHOUT_PHOTO("리뷰"),
    EVENT("이벤트 참여");

    private final String displayName;

    EventCode(String displayName) {
        this.displayName = displayName;
    }

}
