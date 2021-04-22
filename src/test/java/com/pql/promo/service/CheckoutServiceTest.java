package com.pql.promo.service;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.dto.CheckoutResponse;
import com.pql.promo.repository.CartRepository;
import com.pql.promo.strategies.FixedAmountNCartItemsPromo;
import com.pql.promo.strategies.PromoStrategy;
import com.pql.promo.strategies.PromoType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTest {

    @Mock
    CartRepository cartRepository;

    CheckoutService checkoutService;


    @Before
    public void setUp() {
        Map<String, PromoStrategy> promoStrategyMap = new HashMap<>();
        promoStrategyMap.put("SINGLE_ITEM_FIXED", new FixedAmountNCartItemsPromo());
        checkoutService = new CheckoutService(promoStrategyMap);
        checkoutService.cartRepository = cartRepository;

        Cart cart = new Cart();
        cart.setId(12L);
        cart.setReference("ref01");
        cart.setUsername("test username");
        cart.setCartItems(new HashSet<>());

        Promo promo = new Promo();
        promo.setPromoType(PromoType.SINGLE_ITEM_FIXED);
        promo.setNumItems(2);
        promo.setDiscountValue(7);
        promo.setPromoCode("PROMO1");

        Product product = new Product();
        product.setName("product1");
        product.setPrice(new BigDecimal(5));
        product.setSku("TEST123");
        product.setPromo(promo);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setQuantity(3);
        item.setProduct(product);
        cart.getCartItems().add(item);

        when(cartRepository.findByReference(any(String.class))).thenReturn(Optional.of(cart));
    }

    @Test
    public void testCheckout() throws Exception {
        CheckoutResponse response = checkoutService.checkout("ref01");

        assertNotNull(response);
        assertNotNull(response.getOrderId());
        assertEquals(new BigDecimal(15), response.getTotalAmount());
        assertEquals(new BigDecimal(3), response.getTotalDiscount());
        assertEquals(new BigDecimal(12), response.getAmountDue());
    }
}