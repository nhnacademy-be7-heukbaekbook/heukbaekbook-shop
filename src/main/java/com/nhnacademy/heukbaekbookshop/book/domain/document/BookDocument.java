package com.nhnacademy.heukbaekbookshop.book.domain.document;

import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorSummaryResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherSummaryResponse;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Document(indexName = "books")
public class BookDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;


    @Field(type = FieldType.Date)
    private Date publishedAt;

    @Field(type = FieldType.Text)
    private String salePrice;

    @Field(type = FieldType.Float)
    private double discountRate;

    @Field(type = FieldType.Text)
    private String thumbnailUrl;

    @Field(type = FieldType.Nested)
    private List<ContributorSummaryResponse> contributors;

    @Field(type = FieldType.Object)
    private PublisherSummaryResponse publisher;

    // 나중에 추가
//    @Field(type = FieldType.Integer)
//    private int reviewCount;
//
//    @Field(type = FieldType.Integer)
//    private int reviewScore;


    public BookDocument(Long id, String title, Date publishedAt, String salePrice, double discountRate,
                        String thumbnailUrl, List<ContributorSummaryResponse> contributors,
                        PublisherSummaryResponse publisher) {
        this.id = id;
        this.title = title;
        this.publishedAt = publishedAt;
        this.salePrice = salePrice;
        this.discountRate = discountRate;
        this.thumbnailUrl = thumbnailUrl;
        this.contributors = contributors;
        this.publisher = publisher;
    }
//        this.reviewCount = reviewCount; 나중에 추가
//        this.reviewScore = reviewScore;

}
