package com.nhnacademy.heukbaekbookshop.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookRefundPK implements Serializable {
    private Long orderId;
    private Long bookId;
    private Long refundId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBookRefundPK that = (OrderBookRefundPK) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(refundId, that.refundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, bookId, refundId);
    }
}
