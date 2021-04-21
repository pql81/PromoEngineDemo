INSERT INTO PROMOTIONS (id, name, promo_code, promo_type, discount_value, num_items) VALUES (15, 'simple fixed', 'SFX-001', 'SINGLE_ITEM_FIXED', 3, 2);
INSERT INTO PROMOTIONS (id, name, promo_code, promo_type, discount_value, num_items) VALUES (16, 'multi fixed', 'MFX-003', 'MULTIPLE_ITEMS_FIXED', 3, 2);

INSERT INTO PRODUCTS (sku, name, price) VALUES ('AB123', 'cake', 3.50);
INSERT INTO PRODUCTS (sku, name, price, promo_id) VALUES ('CD456', 'pizza', 10.70, 15);
INSERT INTO PRODUCTS (sku, name, price, promo_id) VALUES ('EF889', 'orange juice', 2.10, 16);
INSERT INTO PRODUCTS (sku, name, price, promo_id) VALUES ('GH4745', 'coffee', 1.30, 16);
