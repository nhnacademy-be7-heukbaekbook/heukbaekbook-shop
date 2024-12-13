package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookDocumentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;
@RequiredArgsConstructor
public class BookDocumentRepositoryImpl implements BookDocumentRepositoryCustom {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void deleteAllByIdInIndex(List<Long> ids, String indexName) {
        ids.forEach(id ->
                elasticsearchOperations.delete(String.valueOf(id), IndexCoordinates.of(indexName))
        );
    }

    @Override
    public void saveAllToIndex(List<BookDocument> documents, String indexName) {
        documents.forEach(document ->
                elasticsearchOperations.save(document, IndexCoordinates.of(indexName))
        );
    }
}

