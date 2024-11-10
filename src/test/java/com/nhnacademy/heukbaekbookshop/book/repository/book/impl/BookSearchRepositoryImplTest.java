//package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
//import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
//import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.SearchHitsImpl;
//import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
//
//import java.util.Date;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//class BookSearchRepositoryImplTest {
//
//    @Mock
//    private ElasticsearchOperations elasticsearchOperations;
//
//    @InjectMocks
//    private BookSearchRepositoryImpl bookSearchRepository;
//
//    @Test
//    @DisplayName("데이터베이스 값을 기반으로 도서 검색 테스트")
//    void searchBooks() {
//        // given
//        PageRequest pageable = PageRequest.of(0, 5);
//        String keyword = "스프링";
//        SearchCondition searchCondition = SearchCondition.NONE;
//        SortCondition sortCondition = SortCondition.NEWEST;
//
//        BookDocument bookDocument = new BookDocument(
//                15L,
//                "처음부터 제대로 배우는 스프링 부트 - 자바와 코틀린으로 만나는 클라우드 네이티브 애플리케이션 구축",
//                "<p>설명 1</p>",
//                "<p>목차 1</p>",
//                "9791170000000",
//                new Date(),
//                "출판사",
//                "28000",
//                10.0f,
//                0L,
//                List.of("스프링", "클라우드")
//        );
//
//        SearchHit<BookDocument> searchHit = new SearchHitBuilder<BookDocument>()
//                .withContent(bookDocument)
//                .withIndex("books")
//                .withId("15")
//                .withScore(1.0f)
//                .build();
//
//        SearchHits<BookDocument> searchHits = new SearchHitsImpl<>(
//                1L,
//                1.0f,
//                null,
//                List.of(searchHit)
//        );
//
//        when(elasticsearchOperations.search(any(CriteriaQuery.class), any())).thenReturn(searchHits);
//
//        // when
//        Page<BookDocument> result = bookSearchRepository.search(pageable, keyword, searchCondition, sortCondition);
//
//        // then
//        assertThat(result.getContent()).hasSize(1);
//        BookDocument resultDocument = result.getContent().get(0);
//        assertThat(resultDocument.getId()).isEqualTo(15L);
//        assertThat(resultDocument.getTitle()).isEqualTo("처음부터 제대로 배우는 스프링 부트 - 자바와 코틀린으로 만나는 클라우드 네이티브 애플리케이션 구축");
//        assertThat(resultDocument.getDescription()).isEqualTo("<p>설명 1</p>");
//        assertThat(resultDocument.getIsbn()).isEqualTo("9791170000000");
//        assertThat(resultDocument.getPrice()).isEqualTo("28000");
//        assertThat(resultDocument.getDiscountRate()).isEqualTo(10.0f);
//    }
//}
