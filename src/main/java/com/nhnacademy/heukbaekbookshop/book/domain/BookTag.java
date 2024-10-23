package com.nhnacademy.heukbaekbookshop.book.domain;

import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BookTagPK.class)
@Table(name = "books_tags")
public class BookTag {

    @Id
    @Column(name = "tag_id")
    private long tagId;

    @Id
    @Column(name = "book_id")
    private long bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;
}
