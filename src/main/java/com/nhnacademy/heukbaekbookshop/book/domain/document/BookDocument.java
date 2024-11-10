package com.nhnacademy.heukbaekbookshop.book.domain.document;

import lombok.Getter;
import org.springframework.data.annotation.Id;
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

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String authorName;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Date)
    private Date pubDate;

    @Field(type = FieldType.Keyword)
    private String publisher;

    @Field(type = FieldType.Text)
    private String price;

    @Field(type = FieldType.Float)
    private float discountRate;

    @Field(type = FieldType.Long)
    private Long popularity;

    @Field(type = FieldType.Text)
    private List<String> tags;


    // 나중에 추가
//    @Field(type = FieldType.Integer)
//    private int reviewCount;
//
//    @Field(type = FieldType.Integer)
//    private int reviewScore;


    public BookDocument(Long id, String title, String description, String authorName, String isbn, Date pubDate,
                        String publisher, String price, float discountRate, Long popularity, List<String> tags
                        //int reviewCount, int reviewScore
                        ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorName = authorName;
        this.isbn = isbn;
        this.pubDate = pubDate;
        this.publisher = publisher;
        this.price = price;
        this.discountRate = discountRate;
        this.popularity = popularity;
        this.tags = tags;
//        this.reviewCount = reviewCount; 나중에 추가
//        this.reviewScore = reviewScore;
    }
}
