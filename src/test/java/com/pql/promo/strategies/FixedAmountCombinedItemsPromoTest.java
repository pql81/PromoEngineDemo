package com.pql.promo.strategies;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class FixedAmountCombinedItemsPromoTest {

    @InjectMocks
    FixedAmountCombinedItemsPromo fixedAmountCombinedItemsPromo;


    @Test
    public void testApplyDiscount() throws Exception {
        CartItem item = getCartItemForTest();

        BigDecimal discount = fixedAmountCombinedItemsPromo.applyDiscount(item);

        assertEquals(new BigDecimal(5), discount);
    }

    @Test
    public void testApplyDiscountAsManyTimes() throws Exception {
        CartItem item = getCartItemForTest();
        Cart cart = item.getCart();

        cart.getCartItems().forEach(currentItem ->
            currentItem.setQuantity(16)
        );
        item.setQuantity(18);

        BigDecimal discount = fixedAmountCombinedItemsPromo.applyDiscount(item);

        assertEquals(new BigDecimal(80), discount);
    }

    @Test
    public void testCannotApplyDiscount() throws Exception {
        CartItem item = getCartItemForTest();
        Cart cart = item.getCart();

        Promo promo = item.getProduct().getPromo();

        Product productNeeded = new Product();
        productNeeded.setName("product3");
        productNeeded.setPrice(new BigDecimal(11));
        productNeeded.setSku("TEST333");
        productNeeded.setPromo(promo);

        promo.getProducts().add(productNeeded);

        BigDecimal discount = fixedAmountCombinedItemsPromo.applyDiscount(item);

        assertEquals(BigDecimal.ZERO, discount);
    }

    @Test
    public void testApplyDiscountMoreThanTwoProducts() throws Exception {
        CartItem item = getCartItemForTest();
        Cart cart = item.getCart();

        Promo promo = item.getProduct().getPromo();

        Product product3 = new Product();
        product3.setName("product3");
        product3.setPrice(new BigDecimal(11));
        product3.setSku("TEST333");
        product3.setPromo(promo);

        Product product4 = new Product();
        product4.setName("product4");
        product4.setPrice(new BigDecimal(5));
        product4.setSku("TEST444");
        product4.setPromo(promo);

        promo.getProducts().add(product3);
        promo.getProducts().add(product4);

        CartItem item3 = new CartItem();
        item3.setCart(cart);
        item3.setQuantity(2);
        item3.setProduct(product3);

        CartItem item4 = new CartItem();
        item4.setCart(cart);
        item4.setQuantity(5);
        item4.setProduct(product4);

        cart.getCartItems().add(item3);
        cart.getCartItems().add(item4);

        BigDecimal discount = fixedAmountCombinedItemsPromo.applyDiscount(item);

        assertEquals(new BigDecimal(5), discount);
    }

    @Test
    public void testApplyDiscountProductAlreadyDiscounted() throws Exception {
        CartItem item = getCartItemForTest();
        item.setPromoApplied(true);

        BigDecimal discount = fixedAmountCombinedItemsPromo.applyDiscount(item);

        assertEquals(BigDecimal.ZERO, discount);
    }

    private CartItem getCartItemForTest() {
        Cart cart = new Cart();
        cart.setReference("ref01");
        cart.setUsername("test username");
        cart.setCartItems(new HashSet<>());

        Promo promo = new Promo();
        promo.setPromoType(PromoType.MULTIPLE_ITEMS_FIXED);
        promo.setDiscountValue(5);
        promo.setPromoCode("PROMO2");

        Product product1 = new Product();
        product1.setName("product1");
        product1.setPrice(new BigDecimal(7));
        product1.setSku("TEST123");
        product1.setPromo(promo);

        Product product2 = new Product();
        product2.setName("product2");
        product2.setPrice(new BigDecimal(9));
        product2.setSku("TEST456");
        product2.setPromo(promo);

        Set<Product> productSet = new HashSet<>();
        productSet.add(product1);
        productSet.add(product2);
        promo.setProducts(productSet);

        CartItem item1 = new CartItem();
        item1.setCart(cart);
        item1.setQuantity(1);
        item1.setProduct(product1);

        CartItem item2 = new CartItem();
        item2.setCart(cart);
        item2.setQuantity(2);
        item2.setProduct(product2);

        Set<CartItem> itemSet = new HashSet<>();
        itemSet.add(item1);
        itemSet.add(item2);
        cart.setCartItems(itemSet);

        return item1;
    }
}