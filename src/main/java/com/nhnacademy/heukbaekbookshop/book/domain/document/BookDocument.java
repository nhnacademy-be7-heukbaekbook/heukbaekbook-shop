package com.nhnacademy.heukbaekbookshop.book.domain.document;

import com.nhnacademy.heukbaekbookshop.contributor.domain.BookContributor;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Contributor;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Document(indexName = "books")
public class BookDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private Set<BookContributor> contributors;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Date)
    private Date pubDate;

    @Field(type = FieldType.Keyword)
    private Publisher publisher;

    @Field(type = FieldType.Integer)
    private BigDecimal price;

    @Field(type = FieldType.Float)
    private float discountRate;

    @Field(type = FieldType.Long)
    private Long popularity;

    @Field(type = FieldType.Text)
    private List<String> tags;

    @Field(type = FieldType.Integer)
    private int reviewCount;

    @Field(type = FieldType.Integer)
    private int reviewScore;
}
