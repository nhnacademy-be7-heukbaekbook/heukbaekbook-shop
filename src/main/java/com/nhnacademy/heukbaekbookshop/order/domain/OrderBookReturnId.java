package com.nhnacademy.heukbaekbookshop.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookReturnId {
    private long orderId;
    private long bookId;
    private long returnId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBookReturnId that = (OrderBookReturnId) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(returnId, that.returnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, bookId, returnId);
    }
}
