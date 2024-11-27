package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class BookSearchRepositoryImplTest {

    private ElasticsearchOperations elasticsearchOperations;
    private BookSearchRepositoryImpl bookSearchRepository;

    @BeforeEach
    void setUp() {
        elasticsearchOperations = Mockito.mock(ElasticsearchOperations.class);
        bookSearchRepository = new BookSearchRepositoryImpl(elasticsearchOperations);
    }

    @Test
    void testSearchWithIndexName() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String indexName = "hbbooks_test";
        String keyword = "test";
        SearchCondition searchCondition = SearchCondition.TITLE;
        SortCondition sortCondition = SortCondition.NEWEST;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date publishedAt;
        try {
            publishedAt = dateFormat.parse("2024-11");
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }

        BookDocument bookDocument = new BookDocument(
                1L,
                "Test Title",
                publishedAt,
                100,
                10.0,
                "test-thumbnail-url",
                Arrays.asList("Author1", "Author2"), // 예시 저자 리스트
                "Test Description",
                Arrays.asList(new ContributorSummaryResponse(1L, "Contributor Name")), // 예시 기여자 리스트
                new PublisherSummaryResponse(1L, "Publisher Name"), // 예시 출판사
                1L,
                null,
                null,
                null
        );

        SearchHit<BookDocument> searchHit = new SearchHit<>(
                indexName,
                "1",
                null,
                1.0f,
                null,
                null,
                null,
                null,
                null,
                null,
                bookDocument
        );

        List<SearchHit<BookDocument>> hits = List.of(searchHit);
        SearchHits<BookDocument> searchHits = new SearchHitsImpl<>(
                1,
                TotalHitsRelation.EQUAL_TO,
                1.0f,
                null,
                null,
                hits,
                null,
                null,
                null
        );

        Mockito.when(elasticsearchOperations.search(any(CriteriaQuery.class), Mockito.eq(BookDocument.class), Mockito.eq(IndexCoordinates.of(indexName))))
                .thenReturn(searchHits);

        // when
        Page<BookDocument> result = bookSearchRepository.search(indexName, pageable, keyword, searchCondition, sortCondition, null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Title");
    }
}
