package com.nhnacademy.heukbaekbookshop.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    WAITING_PAYMENT("결제대기"),
    PAYMENT_COMPLETED("결제완료"),
    PENDING("발송준비중"),
    IN_TRANSIT("배송중"),
    DELIVERED("배송완료"),
    RETURNED("반품완료"),
    CANCELLED("취소완료");

    private final String korean; // 한국어 매핑 필드
}
