package com.pql.promo.service;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.Product;
import com.pql.promo.dto.AddCartItemRequest;
import com.pql.promo.dto.AddCartItemResponse;
import com.pql.promo.exception.CartException;
import com.pql.promo.exception.ProductException;
import com.pql.promo.repository.CartItemRepository;
import com.pql.promo.repository.CartRepository;
import com.pql.promo.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartItemServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CartItemService cartItemService;

    @Before
    public void setUp() {
        Cart mockCart = mock(Cart.class);
        Product mockProduct = mock(Product.class);
        when(mockProduct.getName()).thenReturn("test product");
        when(mockProduct.getPrice()).thenReturn(BigDecimal.ONE);

        when(cartRepository.findByReference("test-001")).thenReturn(Optional.of(mockCart));
        when(productRepository.findBySku("abc1")).thenReturn(Optional.of(mockProduct));
    }

    @Test
    public void testAddItemToCart() throws Exception {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setSku("abc1");
        request.setQuantity(3);

        AddCartItemResponse response = cartItemService.addItemToCart("test-001", request);

        assertNotNull(response.getName());
        assertNotNull(response.getPrice());
    }

    @Test(expected = CartException.class)
    public void testFailAddItemToCart() throws Exception {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setSku("abc1");
        request.setQuantity(3);

        cartItemService.addItemToCart("invalid-ref", request);
    }

    @Test(expected = ProductException.class)
    public void testFailProductAddItemToCart() throws Exception {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setSku("invalidSku");
        request.setQuantity(3);

        cartItemService.addItemToCart("test-001", request);
    }
}