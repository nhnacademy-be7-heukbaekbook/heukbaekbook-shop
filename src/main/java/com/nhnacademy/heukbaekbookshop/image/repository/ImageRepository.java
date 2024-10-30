package com.nhnacademy.heukbaekbookshop.image.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
