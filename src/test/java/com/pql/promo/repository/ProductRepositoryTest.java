package com.pql.promo.repository;

import com.pql.promo.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductRepository productRepository;


    @Test
    public void testSaveAndFindProduct() {
        Product product = new Product();
        product.setSku("TEST-SKU");
        product.setName("test");
        product.setPrice(new BigDecimal(5));
        product = entityManager.persistAndFlush(product);

        assertEquals(product, productRepository.findBySku("TEST-SKU").get());
    }
}