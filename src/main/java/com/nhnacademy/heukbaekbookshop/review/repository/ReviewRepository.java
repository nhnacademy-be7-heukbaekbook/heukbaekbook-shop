package com.nhnacademy.heukbaekbookshop.review.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.Review;
import com.nhnacademy.heukbaekbookshop.order.domain.ReviewPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewPK> {
    List<Review> findByBookId(Long bookId);
}
