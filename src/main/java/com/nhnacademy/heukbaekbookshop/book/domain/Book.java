package com.nhnacademy.heukbaekbookshop.book.domain;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "book_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "book_title")
    private String title;

    @NotNull
    @Column(name = "book_index")
    private String index;

    @NotNull
    @Column(name = "book_description")
    private String description;

    @NotNull
    @Column(name = "book_publication")
    private Date publication;

    @NotNull
    @Length(min = 13, max = 13)
    @Column(name = "book_isbn")
    private String isbn;

    @NotNull
    @Column(name = "is_packable")
    private boolean isPackable;

    @NotNull
    @Column(name = "book_stock")
    private int stock;

    @NotNull
    @Column(name = "book_price")
    private BigDecimal price;

    @NotNull
    @Column(name = "book_discount_rate")
    private float discountRate;

    @NotNull
    @Column(name = "book_popularity")
    private long popularity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus status;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> tags;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> like;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cart> carts;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCategory> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderBook> orderBooks;
}
