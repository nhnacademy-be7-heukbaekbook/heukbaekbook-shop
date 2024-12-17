package com.nhnacademy.heukbaekbookshop.book.domain;

import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.contributor.domain.BookContributor;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.couponset.coupon.domain.BookCoupon;
import com.nhnacademy.heukbaekbookshop.image.domain.BookImage;
import com.nhnacademy.heukbaekbookshop.order.domain.OrderBook;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_title")
    private String title;

    @Column(name = "book_index")
    private String index;

    @Column(name = "book_description")
    private String description;

    @Column(name = "book_published_at")
    private Date publishedAt;

    @Column(name = "book_isbn")
    private String isbn;

    @Column(name = "is_packable")
    private boolean isPackable;

    @Column(name = "book_stock")
    private int stock;

    @Column(name = "book_price")
    private BigDecimal price;

    @Column(name = "book_discount_rate")
    private BigDecimal discountRate;

    @Column(name = "book_popularity")
    private long popularity;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> like = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cart> carts = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderBook> orderBooks = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookContributor> contributors = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookImage> bookImages = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCoupon> bookCoupons = new HashSet<>();

    public Book(Long id, String title, String index, String description, Date publishedAt, String isbn, boolean isPackable, int stock, BigDecimal price, BigDecimal discountRate, long popularity, BookStatus status) {
        this(id, title, index, description, publishedAt, isbn, isPackable, stock, price, discountRate, popularity, status, null);
    }

    public Book(Long id, String title, String index, String description, Date publishedAt, String isbn, boolean isPackable, int stock, BigDecimal price, BigDecimal discountRate, long popularity, BookStatus status, Publisher publisher) {
        this.id = id;
        this.title = title;
        this.index = index;
        this.description = description;
        this.publishedAt = publishedAt;
        this.isbn = isbn;
        this.isPackable = isPackable;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.popularity = popularity;
        this.discountRate = discountRate;
        this.publisher = publisher;
    }

    public void addCategory(BookCategory bookCategory) {
        bookCategory.setBook(this);
        categories.add(bookCategory);
    }

    public void addBookImage(BookImage bookImage) {
        bookImage.setBook(this);
        this.bookImages.add(bookImage);
    }

    public void removeBookImage(BookImage bookImage) {
        bookImage.setBook(null);
        this.bookImages.remove(bookImage);
    }

    public void addTag(BookTag bookTag) {
        bookTag.setBook(this);
        this.tags.add(bookTag);
    }
}
