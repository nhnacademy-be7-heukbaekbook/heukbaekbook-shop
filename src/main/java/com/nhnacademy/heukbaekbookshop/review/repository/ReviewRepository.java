package com.nhnacademy.heukbaekbookshop.review.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByBookId(Long bookId);
}
