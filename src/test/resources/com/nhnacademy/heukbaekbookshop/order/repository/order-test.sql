-- Publisher 데이터 삽입
INSERT INTO publishers (publisher_id, publisher_name)
VALUES (1, 'NHN Academy Publisher');

-- Category 데이터 삽입
INSERT INTO categories (category_id, parent_category_id, category_name)
VALUES
    (1, null, '상위카테고리1'),
    (2, null, '상위카테고리2'),
    (3, null, '상위카테고리3'),
    (4, 1, '하위카테고리1'),
    (5, 1, '하위카테고리2'),
    (6, 1, '하위카테고리3');

-- Book 데이터 삽입
INSERT INTO books (book_id, book_title, book_index, book_description, book_published_at, book_isbn, is_packable, book_stock, book_price, book_discount_rate, book_popularity, book_status, publisher_id)
VALUES
    (1, 'Java Programming', '001', 'Learn Java Programming', '2023-01-01', '9781234567897', TRUE, 100, 30000.00, 10.00, 0, 'IN_STOCK', 1),
    (2, 'Spring Framework', '002', 'Learn Spring Framework', '2023-02-01', '9789876543210', TRUE, 50, 40000.00, 15.00, 0, 'IN_STOCK', 1),
    (3, 'Hibernate in Action', '003', 'Master Hibernate', '2022-05-15', '9781122334455', FALSE, 30, 25000.00, 5.00, 0, 'IN_STOCK', 1);

-- BookCategory 데이터 삽입
INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);


INSERT INTO images (image_id, image_url, image_type)
VALUES
    (1, 'http://example.com/java_thumbnail.jpg', 'THUMBNAIL'),
    (2, 'http://example.com/spring_thumbnail.jpg', 'DETAIL'),
    (3, 'http://example.com/hibernate_thumbnail.jpg', 'THUMBNAIL');

-- Book Image 데이터 삽입
INSERT INTO books_images (image_id, book_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

INSERT INTO delivery_fees (delivery_fee_id, delivery_fee_name, delivery_fee, minimum_order_amount)
VALUES
    (1, '기본 배송료', 5000, 0),
    (2, '5만원 이상 무료 배송', 0, 50000);