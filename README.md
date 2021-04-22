# PromoEngineDemo
Demo for e-commerce platform implementing scalable promotion engine

#### Prerequisites

Application runs with Java8, SpringBoot v2.4.5 and Maven.

#### Running the application with Maven

```shell
$ git clone https://github.com/pql81/PromoEngineDemo.git
$ cd PromoEngineDemo
$ git checkout master
$ mvn spring-boot:run
```

#### OpenAPI documetation and UI test

OpenAPI description can be found at http://localhost:8080/v3/api-docs/

Testing API is possible at http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
The URL above works as API client to test applcition functionalities

#### Running test for required use case

To run all test:
```shell
$ cd PromoEngineDemo
$ mvn test
```

To run only tests for the use case (2 different promotion strategies):
```shell
$ cd PromoEngineDemo
$ mvn -Dtest=PromoEngineDemoTest test
```

#### Accessing the H2 console

URL to H2 console: ***http://localhost:8080/h2-console***
Connection URL is ***jdbc:h2:mem:promodb***
Credentials are default ones, user 'sa' and no password

#### Database schema

```sql
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
```
