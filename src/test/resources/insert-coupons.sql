DELETE
FROM book_coupon;
DELETE
FROM category_coupon;
DELETE
FROM coupon;

INSERT INTO coupon (code, name, discount_amount, discount_type, max_discount_amount, min_purchase_amount, started_at,
                    expiration_at, created_at, updated_at)
VALUES ('G123456789012345', '일반 쿠폰 1', 1000, 'AMOUNT', 5000, 10000, '2023-01-01 00:00:00', '2023-12-31 00:00:00',
        '2023-01-01 00:00:00', '2023-01-01 00:00:00'),
       ('B123456789012345', '책 쿠폰 1', 1500, 'AMOUNT', 7000, 5000, '2023-01-01 00:00:00', '2023-12-31 00:00:00',
        '2023-01-01 00:00:00', '2023-01-01 00:00:00'),
       ('C123456789012345', '카테고리 쿠폰 1', 2000, 'PERCENT', 8000, 4000, '2023-01-01 00:00:00', '2023-12-31 00:00:00',
        '2023-01-01 00:00:00', '2023-01-01 00:00:00');

INSERT INTO book_coupon (coupon_code, book_id, created_at, updated_at)
VALUES ('B123456789012345', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

INSERT INTO category_coupon (coupon_code, category_id, created_at, updated_at)
VALUES ('C123456789012345', 2, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
