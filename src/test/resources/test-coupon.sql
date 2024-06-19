DELETE
FROM category_coupon;
DELETE
FROM book_coupon;
DELETE
FROM coupon;

INSERT INTO coupon (code, name, discount_amount, discount_type, max_discount_amount, min_purchase_amount, started_at,
                    expiration_at, created_at, updated_at)
VALUES ('C123456789012345', 'Test Coupon', 1000, 'AMOUNT', 5000, 10000, '2023-06-01 00:00:00', '2024-06-01 00:00:00',
        '2023-06-01 00:00:00', '2023-06-01 00:00:00');
