package com.nhnacademy.heukbaekbookshop.order.domain;

import com.nhnacademy.heukbaekbookshop.order.exception.OrderStatusNotFoundException;
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
    CANCELED("취소완료");

    private final String korean; // 한국어 매핑 필드

    public static OrderStatus fromKorean(String korean) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getKorean().equals(korean)) {
                return status;
            }
        }
        throw new OrderStatusNotFoundException(korean + " order status not found");
    }
}
