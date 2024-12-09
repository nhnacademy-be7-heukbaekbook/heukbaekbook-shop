package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookDocumentRepositoryImplTest {

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private BookDocumentRepositoryImpl bookDocumentRepository;

    @Captor
    private ArgumentCaptor<String> idCaptor;

    @Captor
    private ArgumentCaptor<BookDocument> documentCaptor;

    @Captor
    private ArgumentCaptor<IndexCoordinates> indexCoordinatesCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteAllByIdInIndex_Success() {
        // Given
        List<Long> ids = List.of(1L, 2L, 3L);
        String indexName = "test-index";

        // When
        bookDocumentRepository.deleteAllByIdInIndex(ids, indexName);

        // Then
        verify(elasticsearchOperations, times(ids.size()))
                .delete(idCaptor.capture(), indexCoordinatesCaptor.capture());

        List<String> capturedIds = idCaptor.getAllValues();
        List<IndexCoordinates> capturedIndexCoordinates = indexCoordinatesCaptor.getAllValues();

        for (int i = 0; i < ids.size(); i++) {
            assertEquals(String.valueOf(ids.get(i)), capturedIds.get(i));
            assertEquals(IndexCoordinates.of(indexName), capturedIndexCoordinates.get(i));
        }
    }

    @Test
    void saveAllToIndex_Success() {
        // Given
        BookDocument doc1 = createTestBookDocument(1L);
        BookDocument doc2 = createTestBookDocument(2L);
        List<BookDocument> documents = List.of(doc1, doc2);
        String indexName = "test-index";

        // When
        bookDocumentRepository.saveAllToIndex(documents, indexName);

        // Then
        verify(elasticsearchOperations, times(documents.size()))
                .save(documentCaptor.capture(), indexCoordinatesCaptor.capture());

        List<BookDocument> capturedDocuments = documentCaptor.getAllValues();
        List<IndexCoordinates> capturedIndexCoordinates = indexCoordinatesCaptor.getAllValues();

        for (int i = 0; i < documents.size(); i++) {
            assertEquals(documents.get(i), capturedDocuments.get(i));
            assertEquals(IndexCoordinates.of(indexName), capturedIndexCoordinates.get(i));
        }
    }

    // Helper method to create a BookDocument
    private BookDocument createTestBookDocument(Long id) {
        return new BookDocument(
                id,
                "Test Book " + id,
                null,
                10000,
                0.1,
                "http://example.com/image" + id + ".jpg",
                List.of("Author " + id),
                "Test Description " + id,
                null,
                null,
                100L,
                List.of(1L, 2L),
                10,
                4.5f
        );
    }
}
