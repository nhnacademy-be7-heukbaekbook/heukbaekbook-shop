package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;

import java.util.List;

public interface BookDocumentRepositoryCustom {
    void deleteAllByIdInIndex(List<Long> ids, String indexName);
    void saveAllToIndex(List<BookDocument> documents, String indexName);
}
