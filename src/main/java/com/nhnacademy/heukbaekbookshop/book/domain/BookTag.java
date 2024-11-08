package com.nhnacademy.heukbaekbookshop.book.domain;

import com.nhnacademy.heukbaekbookshop.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BookTagPK.class)
@Table(name = "books_tags")
public class BookTag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookTag)) return false;
        BookTag that = (BookTag) o;
        return Objects.equals(book.getId(), that.book.getId()) &&
                Objects.equals(tag.getId(), that.tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(book.getId(), tag.getId());
    }
}

