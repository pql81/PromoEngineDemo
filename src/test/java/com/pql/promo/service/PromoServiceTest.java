package com.pql.promo.service;

import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.dto.GetPromoResponse;
import com.pql.promo.exception.ProductException;
import com.pql.promo.repository.ProductRepository;
import com.pql.promo.repository.PromoRepository;
import com.pql.promo.strategies.PromoType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class PromoServiceTest {

    @Mock
    PromoRepository promoRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    PromoService promoService;

    @Before
    public void setUp(){
        Product mockProduct = mock(Product.class);

        Promo promo = new Promo();
        promo.setName("test promo");
        promo.setPromoCode("PROMO5");
        promo.setNumItems(3);
        promo.setPromoType(PromoType.SINGLE_ITEM_FIXED);
        promo.setDiscountValue(5);

        Set<Product> productSet = new HashSet<>();
        productSet.add(mockProduct);
        promo.setProducts(productSet);

        when(productRepository.findBySku("abc01")).thenReturn(Optional.of(mockProduct));
        when(promoRepository.findByProductsAndPromoCode(mockProduct, "PROMO5")).thenReturn(Optional.of(promo));
    }

    @Test
    public void testGetPromoForProduct() throws Exception {
        GetPromoResponse response = promoService.getPromoForProduct("abc01", "PROMO5");

        assertEquals("test promo", response.getName());
        assertEquals("PROMO5", response.getPromoCode());
        assertEquals(PromoType.SINGLE_ITEM_FIXED.name(), response.getPromoType());
        assertNotNull(response.getSkus());
    }

    @Test(expected = ProductException.class)
    public void testFailGetPromoForProduct() throws Exception {
        promoService.getPromoForProduct("invalidSku", "PROMO5");
    }
}