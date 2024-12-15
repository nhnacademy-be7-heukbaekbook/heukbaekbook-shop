package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.image.domain.Image;
import com.nhnacademy.heukbaekbookshop.image.domain.ImageType;
import com.nhnacademy.heukbaekbookshop.image.domain.WrappingPaperImage;
import com.nhnacademy.heukbaekbookshop.order.domain.WrappingPaper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class WrappingPaperRepositoryCustomTest {

    @Autowired
    private WrappingPaperRepository wrappingPaperRepository;

    @Autowired
    private WrappingPaperImageRepository wrappingPaperImageRepository;

    @BeforeEach
    void setUp() {
        WrappingPaper wrappingPaper1 = WrappingPaper.createWrappingPaper("name1", BigDecimal.valueOf(500));
        WrappingPaper wrappingPaper2 = WrappingPaper.createWrappingPaper("name2", BigDecimal.valueOf(2000));

        wrappingPaperRepository.save(wrappingPaper1);
        wrappingPaperRepository.save(wrappingPaper2);


        WrappingPaperImage wrappingPaperImage1 = new WrappingPaperImage(wrappingPaper1);
        wrappingPaperImage1.setUrl("url1");
        wrappingPaperImage1.setType(ImageType.THUMBNAIL);

        WrappingPaperImage wrappingPaperImage2 = new WrappingPaperImage(wrappingPaper2);
        wrappingPaperImage2.setUrl("url2");
        wrappingPaperImage2.setType(ImageType.THUMBNAIL);

        wrappingPaperImageRepository.save(wrappingPaperImage1);
        wrappingPaperImageRepository.save(wrappingPaperImage2);
    }

    @Test
    void searchById() {
        //given
        Long wrappingPaperId = 1L;

        //when
        Optional<WrappingPaper> result = wrappingPaperRepository.searchById(wrappingPaperId);

        //then
        assertTrue(result.isPresent());
        assertEquals(wrappingPaperId, result.get().getId());
    }

    @Test
    void searchAll() {
        //given

        //when
        List<WrappingPaper> wrappingPapers = wrappingPaperRepository.searchAll();

        //then
        assertNotNull(wrappingPapers);
        assertEquals(2, wrappingPapers.size());
    }
}