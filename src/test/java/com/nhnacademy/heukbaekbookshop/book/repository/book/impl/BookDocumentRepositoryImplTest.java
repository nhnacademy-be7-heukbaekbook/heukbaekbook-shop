package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookDocumentRepositoryImplTest {

    @InjectMocks
    private BookDocumentRepositoryImpl bookDocumentRepository;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    void deleteAllByIdInIndex_shouldDeleteDocumentsByIds() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);
        String indexName = "books";

        // when
        bookDocumentRepository.deleteAllByIdInIndex(ids, indexName);

        // then
        ids.forEach(id ->
                Mockito.verify(elasticsearchOperations).delete(String.valueOf(id), IndexCoordinates.of(indexName))
        );
    }

    @Test
    void saveAllToIndex_shouldSaveDocumentsToIndex() {
        // given
        String indexName = "books";
        List<BookDocument> documents = List.of(
                new BookDocument(1L, "title", Date.valueOf(LocalDate.now()), 10000, 10.0, "thumbnailUrl", null, null, null, null, null, null, null, null),
                new BookDocument(2L, "title", Date.valueOf(LocalDate.now()), 10000, 10.0, "thumbnailUrl", null, null, null, null, null, null, null, null)
        );

        // when
        bookDocumentRepository.saveAllToIndex(documents, indexName);

        // then
        documents.forEach(document ->
                Mockito.verify(elasticsearchOperations).save(document, IndexCoordinates.of(indexName))
        );
    }
}