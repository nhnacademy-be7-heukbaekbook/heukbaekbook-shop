package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.WrappingPaperImage;
import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WrappingPaperImageRepository extends JpaRepository<WrappingPaperImage, Long> {
    Optional<WrappingPaperImage> findByWrappingPaper(WrappingPaper wrappingPaper);

    void deleteByWrappingPaper(WrappingPaper wrappingPaper);

}