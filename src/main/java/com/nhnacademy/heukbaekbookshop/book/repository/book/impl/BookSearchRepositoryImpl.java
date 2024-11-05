package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;


import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BookSearchRepositoryImpl implements BookSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public BookSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Page<BookDocument> search(Pageable pageable, String keyword, SearchCondition searchCondition, SortCondition sortCondition) {
        // Criteria 생성 (검색 조건)
        Criteria criteria = Criteria.where(searchCondition.name().toLowerCase()).contains(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(pageable)
                .addSort(resolveSort(sortCondition));

        // ElasticsearchOperations로 검색 실행
        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query, BookDocument.class);

        // Page 객체로 변환
        return PageableExecutionUtils.getPage(
                searchHits.map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList(),
                pageable,
                searchHits::getTotalHits);
    }

    private Sort resolveSort(SortCondition sortCondition) {
        return switch (sortCondition) {
            case POPULARITY -> Sort.by(Sort.Order.desc("popularity"));
            case NEWEST -> Sort.by(Sort.Order.desc("publishDate"));
            case LOWEST_PRICE -> Sort.by(Sort.Order.asc("price"));
            case HIGHEST_PRICE -> Sort.by(Sort.Order.desc("price"));
            case RATING -> Sort.by(Sort.Order.desc("rating"));
            case REVIEW_COUNT -> Sort.by(Sort.Order.desc("reviewCount"));
            default -> throw new IllegalArgumentException("Unknown sort condition: " + sortCondition);
        };
    }
}