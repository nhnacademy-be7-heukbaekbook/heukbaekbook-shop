package com.nhnacademy.heukbaekbookshop.book.repository.book;

import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long>, BookDocumentRepositoryCustom {
}
