package com.pql.promo.repository;

import com.pql.promo.domain.Cart;
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
public class CartRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CartRepository cartRepository;


    @Test
    public void testSaveAndFindCart() {
        Cart cart = new Cart();
        cart.setReference("ref01");
        cart.setUsername("test username");
        cart = entityManager.persistAndFlush(cart);

        assertEquals(cart, cartRepository.findByReference("ref01").get());
    }
}