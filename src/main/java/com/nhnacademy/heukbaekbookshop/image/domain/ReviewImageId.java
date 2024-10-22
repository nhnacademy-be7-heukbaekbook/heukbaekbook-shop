package com.nhnacademy.heukbaekbookshop.image.domain;

import com.nhnacademy.heukbaekbookshop.coupon.domain.BookCouponId;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewId;
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
public class ReviewImageId implements Serializable {
    private long imageId;
    private long customerId;
    private long bookId;
    private long orderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewImageId that = (ReviewImageId) o;
        return Objects.equals(imageId, that.imageId) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, customerId, orderId, bookId);
    }

}
