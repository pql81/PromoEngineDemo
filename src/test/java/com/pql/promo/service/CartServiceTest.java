package com.pql.promo.service;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.dto.CreateCartRequest;
import com.pql.promo.dto.CreateCartResponse;
import com.pql.promo.dto.GetCartItemDTO;
import com.pql.promo.dto.GetCartResponse;
import com.pql.promo.repository.CartRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @InjectMocks
    CartService cartService;

    @Before
    public void setUp() {
        Cart cart1 = new Cart();
        cart1.setId(12L);
        cart1.setReference("ref01");
        cart1.setUsername("test username");
        cart1.setCartItems(new HashSet<>());

        Cart cart2 = new Cart();
        cart2.setId(15L);
        cart2.setReference("ref02");
        cart2.setUsername("test username");
        cart2.setCartItems(new HashSet<>());

        when(cartRepository.findByReference(cart1.getReference())).thenReturn(Optional.of(cart1));
        when(cartRepository.findByReference(cart2.getReference())).thenReturn(Optional.of(cart1));
    }

    @Test
    public void testGetCart() throws Exception {
        GetCartResponse response1 = cartService.getCart("ref01");
        GetCartResponse response2 = cartService.getCart("ref02");

        assertEquals("test username", response1.getUsername());
        assertNotNull("ref01", response1.getReference());
        assertEquals("test username", response2.getUsername());
        assertNotNull("ref02", response2.getReference());
    }

    @Test
    public void testEmptyGetCart() throws Exception {
        GetCartResponse response = cartService.getCart("notExisting");

        assertNull(response);
    }

    @Test
    public void testGetCartItemsFromCart() throws Exception {
        Cart cart = new Cart();
        Set<CartItem> itemSet = new HashSet<>();

        CartItem mockItem1 = mock(CartItem.class);
        when(mockItem1.getTotalPrice()).thenReturn(BigDecimal.ONE);
        when(mockItem1.getQuantity()).thenReturn(3);
        itemSet.add(mockItem1);

        CartItem mockItem2 = mock(CartItem.class);
        when(mockItem2.getTotalPrice()).thenReturn(BigDecimal.ONE);
        when(mockItem1.getQuantity()).thenReturn(1);
        itemSet.add(mockItem2);

        cart.setCartItems(itemSet);

        Set<GetCartItemDTO> items = cartService.getCartItemsFromCart(cart);

        assertEquals(2, items.size());
        items.forEach(item ->
            assertEquals(BigDecimal.ONE, item.getPrice())
        );
    }

    @Test
    public void testCreateCart() throws Exception {
        CreateCartRequest request = new CreateCartRequest();
        request.setUsername("test username");

        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());

        CreateCartResponse response = cartService.createCart(request);

        assertNotNull(response.getReference());
    }
}