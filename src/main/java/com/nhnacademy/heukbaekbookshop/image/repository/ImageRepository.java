package com.nhnacademy.heukbaekbookshop.image.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByIdAndType(Long id, ImageType type);
}
