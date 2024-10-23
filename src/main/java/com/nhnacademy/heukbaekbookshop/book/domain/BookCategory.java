package com.nhnacademy.heukbaekbookshop.book.domain;

import com.nhnacademy.heukbaekbookshop.category.domain.Category;
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
@IdClass(BookCategoryPK.class)
@Table(name = "books_categories")
public class BookCategory {

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "category_id")
    private long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

}
