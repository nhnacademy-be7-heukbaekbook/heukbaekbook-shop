package com.nhnacademy.heukbaekbookshop.review.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
