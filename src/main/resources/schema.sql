DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS CARTS;
DROP TABLE IF EXISTS CART_ITEMS;
DROP TABLE IF EXISTS PROMO;


CREATE TABLE PROMOTIONS (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  promo_code VARCHAR(100) NOT NULL,
  promo_type VARCHAR(100) NOT NULL,
  num_items INT,
  discount_value INT NOT NULL
);

CREATE TABLE PRODUCTS (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sku VARCHAR(100) NOT NULL,
  name VARCHAR(250) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  promo_id INT,
  FOREIGN KEY (promo_id) references PROMOTIONS(id)
);

CREATE TABLE CARTS (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  reference VARCHAR(100) NOT NULL
);

CREATE TABLE CART_ITEMS (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cart_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  FOREIGN KEY (cart_id) references CARTS(id),
  FOREIGN KEY (product_id) references PRODUCTS(id)
);
