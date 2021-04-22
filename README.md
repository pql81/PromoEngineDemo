# PromoEngineDemo
Demo for e-commerce platform implementing scalable promotion engine

The purpose of the project is to implement a scalable engine for promotions in an e-commerce application.
It allows to add or remove promotions for single and multiple products.
Implementation of a new type of promotion, cart based or item based is possible just implementing the dedicated interface.

In order to achieve the modularity goal, the promotion engine implements the strategy pattern. An interface ```PromoStrategy``` is used to define how the checkout retrieves the total discount to apply to the cart.
Current implementations of the interface are:
* ```FixedAmountNCartItemsPromo``` get N items of a product and pay a fixed amount
* ```FixedAmountCombinedItemsPromo``` get items A, B, C... and pay a fixed amount

The enum ```PromoType``` contains keys to the selection map in ```CheckoutService```. Each promotion type implementation has to be annotated as ```@Component("PROMO_TYPE_KEY")``` in order to be recognised and handled.

The selection of the right promotion strategy is delegated to Spring through the strategy map ```promoStrategyMap``` in ```CheckoutService```.

To implement a new strategy:
- Create a new class implementing ```PromoStrategy```
- Implement the method ```public BigDecimal applyDiscount(CartItem cartItem)``` in the new component
- Add a new entry <NEW_PROMOTION_TYPE> in the ```PromoType``` Enum
- Annotate the new strategy implementation class with ```@Component("<NEW_PROMOTION_TYPE>")```

Spring can now populate the ```promoStrategyMap``` in ```CheckoutService``` including the newly created component.

To use the new strategy simply add a record to the Database:

```sql
INSERT INTO PROMOTIONS (name, promo_code, promo_type, discount_value, num_items)
VALUES ('my new promo', 'PROMO123', 'SINGLE_ITEM_PERCENT', 30, 2);
```

The current design allows a product to be registered for 1 promotion at time. A promotion can include more than 1 product, like a combination of products A, B and C.

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
