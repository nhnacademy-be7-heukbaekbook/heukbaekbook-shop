package com.nhnacademy.heukbaekbookshop.book.repository.book.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.SearchCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.SortCondition;
import com.nhnacademy.heukbaekbookshop.book.domain.document.BookDocument;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookSearchRepository;
import com.nhnacademy.heukbaekbookshop.book.util.SynonymUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookSearchRepositoryImpl implements BookSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;


    public BookSearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Page<BookDocument> search(Pageable pageable, String keyword, SearchCondition searchCondition, SortCondition sortCondition) {
        Criteria criteria = new Criteria();

        if (searchCondition == SearchCondition.ALL) {
            // 통합 검색: 여러 필드에 대해 검색 조건 추가
            criteria = criteria.or(Criteria.where("title").contains(keyword).boost(100))
                    .or(Criteria.where("description").contains(keyword).boost(50))
                    .or(Criteria.where("author").contains(keyword).boost(75))
                    .or(Criteria.where("tags").contains(keyword).boost(50));
        } else {
            // 특정 필드에 대한 검색
            criteria = criteria.or(Criteria.where(searchCondition.name().toLowerCase()).contains(keyword));
        }
        // 동의어 추가
        List<String> synonyms = SynonymUtil.getSynonyms(keyword);
        for (String synonym : synonyms) {
            criteria = criteria.or(Criteria.where("title").contains(synonym).boost(100))
                    .or(Criteria.where("description").contains(synonym).boost(10))
                    .or(Criteria.where("tags").contains(synonym).boost(50))
                    .or(Criteria.where(searchCondition.name().toLowerCase()).contains(synonym));
        }

        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(pageable)
                .addSort(resolveSort(sortCondition));

        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query, BookDocument.class);

        return PageableExecutionUtils.getPage(
                searchHits.map(org.springframework.data.elasticsearch.core.SearchHit::getContent).toList(),
                pageable,
                searchHits::getTotalHits
        );
    }


    private Sort resolveSort(SortCondition sortCondition) {
        return switch (sortCondition) {
            case NEWEST -> Sort.by(Sort.Order.desc("publishedAt"));
            case LOWEST_PRICE -> Sort.by(Sort.Order.asc("salePrice"));
            case HIGHEST_PRICE -> Sort.by(Sort.Order.desc("salePrice"));
//            case RATING -> Sort.by(Sort.Order.desc("reviewScore"));
//            case REVIEW_COUNT -> Sort.by(Sort.Order.desc("reviewCount"));
            default -> Sort.by(Sort.Order.desc("publishedAt"));
        };
    }


}