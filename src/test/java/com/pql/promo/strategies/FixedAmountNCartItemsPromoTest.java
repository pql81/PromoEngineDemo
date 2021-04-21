package com.pql.promo.strategies;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.service.CheckoutService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class FixedAmountNCartItemsPromoTest {

    @InjectMocks
    FixedAmountNCartItemsPromo fixedAmountNCartItemsPromo;


    @Test
    public void testApplyDiscount() throws Exception {
        CartItem item = getCartItemForTest();

        BigDecimal discount = fixedAmountNCartItemsPromo.applyDiscount(item);

        assertEquals(new BigDecimal(3), discount);
    }

    @Test
    public void testApplyDiscountAsManyTimes() throws Exception {
        CartItem item = getCartItemForTest();
        item.setQuantity(25);
        item.getProduct().getPromo().setNumItems(4);

        BigDecimal discount = fixedAmountNCartItemsPromo.applyDiscount(item);

        assertEquals(new BigDecimal(6*3), discount);
    }

    @Test
    public void testCannotApplyDiscount() throws Exception {
        CartItem item = getCartItemForTest();
        item.getProduct().getPromo().setNumItems(4);

        BigDecimal discount = fixedAmountNCartItemsPromo.applyDiscount(item);

        assertEquals(BigDecimal.ZERO, discount);
    }

    @Test
    public void testApplyDiscountProductAlreadyDiscounted() throws Exception {
        CartItem item = getCartItemForTest();
        item.setPromoApplied(true);

        BigDecimal discount = fixedAmountNCartItemsPromo.applyDiscount(item);

        assertEquals(BigDecimal.ZERO, discount);
    }

    private CartItem getCartItemForTest() {
        Cart cart = new Cart();
        cart.setReference("ref01");
        cart.setUsername("test username");
        cart.setCartItems(new HashSet<>());

        Promo promo = new Promo();
        promo.setPromoType(PromoType.SINGLE_ITEM_FIXED);
        promo.setNumItems(2);
        promo.setDiscountValue(3);
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

        return item;
    }
}