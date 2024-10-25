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
public class OrderBookReturnPK implements Serializable {
    private Long orderId;
    private Long bookId;
    private Long returnId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBookReturnPK that = (OrderBookReturnPK) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(returnId, that.returnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, bookId, returnId);
    }
}
