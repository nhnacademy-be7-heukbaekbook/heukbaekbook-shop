package com.nhnacademy.heukbaekbookshop.image.domain;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
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
@IdClass(BookImageId.class)
@Table(name = "books_images")
public class BookImage {

    @Id
    @Column(name = "image_id")
    private long imageId;

    @Id
    @Column(name = "book_id")
    private long bookId;

    @OneToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

}
