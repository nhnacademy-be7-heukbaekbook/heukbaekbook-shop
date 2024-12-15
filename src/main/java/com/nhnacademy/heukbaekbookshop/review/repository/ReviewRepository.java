package com.nhnacademy.heukbaekbookshop.review.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewPK> {
    List<Review> findAllByBookId(Long bookId);
    List<Review> findAllByCustomerId(Long customerId);

    Review findByOrderIdAndBookIdAndCustomerId(Long customerId, Long orderId, Long bookId);
    List<Review> findAllByOrderId(Long orderId);
}