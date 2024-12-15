package com.nhnacademy.heukbaekbookshop.book.domain.document;

import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
//@Document(indexName = "#{@indexNameProvider.resolveIndexName()}") // IndexNameProvider 사용
public class BookDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Date)
    private Date publishedAt;

    @Field(type = FieldType.Integer)
    private Integer salePrice;

    @Field(type = FieldType.Float)
    private double discountRate;

    @Field(type = FieldType.Text)
    private String thumbnailUrl;

    @Field(type = FieldType.Text)
    private List<String> author;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Nested)
    private List<ContributorSummaryResponse> contributors;

    @Field(type = FieldType.Object)
    private PublisherSummaryResponse publisher;


    @Field(type = FieldType.Long)
    private Long popularity;

    @Field(type = FieldType.Long)
    private List<Long> categoryId;


    @Field(type = FieldType.Integer)
    private Integer reviewCount;

    @Field(type = FieldType.Float)
    private Float reviewScore;


    public BookDocument(Long id, String title, Date publishedAt, int salePrice, double discountRate,
                        String thumbnailUrl, List<String> author, String description, List<ContributorSummaryResponse> contributors,
                        PublisherSummaryResponse publisher, Long popularity, List<Long> categoryId, Integer reviewCount, Float reviewScore) {
        this.id = id;
        this.title = title;
        this.publishedAt = publishedAt;
        this.salePrice = salePrice;
        this.discountRate = discountRate;
        this.thumbnailUrl = thumbnailUrl;
        this.author = author;
        this.description = description;
        this.contributors = contributors;
        this.publisher = publisher;
        this.popularity = popularity;
        this.categoryId = categoryId;
        this.reviewCount = (reviewCount != null) ? reviewCount : 0;
        this.reviewScore = (reviewScore != null) ? reviewScore : 0.0f;
    }
}


