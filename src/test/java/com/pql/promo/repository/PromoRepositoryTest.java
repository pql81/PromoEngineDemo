package com.pql.promo.repository;

import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.strategies.PromoType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PromoRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PromoRepository promoRepository;


    @Test
    public void testSaveAndFindPromo() {
        Promo promo = new Promo();
        promo.setName("test promo");
        promo.setPromoCode("CODE2");
        promo.setPromoType(PromoType.SINGLE_ITEM_FIXED);
        promo.setNumItems(4);
        promo.setDiscountValue(5);
        promo = entityManager.persistAndFlush(promo);

        assertEquals(promo, promoRepository.findById(promo.getId()).get());
    }
}
