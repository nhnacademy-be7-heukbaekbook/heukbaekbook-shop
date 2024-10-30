package com.nhnacademy.heukbaekbookshop.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
