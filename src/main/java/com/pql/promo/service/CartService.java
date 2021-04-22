package com.pql.promo.service;

import com.pql.promo.domain.Cart;
import com.pql.promo.dto.CreateCartRequest;
import com.pql.promo.dto.CreateCartResponse;
import com.pql.promo.dto.GetCartItemDTO;
import com.pql.promo.dto.GetCartResponse;
import com.pql.promo.repository.CartRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Log4j2
@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public GetCartResponse getCart(String reference) {
        Optional<Cart> cart0 = cartRepository.findByReference(reference);

        GetCartResponse response = null;

        if (cart0.isPresent()) {
            Cart cart = cart0.get();
            response = new GetCartResponse(cart.getUsername(), cart.getReference());
            response.setCartItems(getCartItemsFromCart(cart));
        } else {
            log.warn("Cart not found - reference: " + reference);
        }

        return response;
    }

    public Set<GetCartItemDTO> getCartItemsFromCart(Cart cart) {
        Set<GetCartItemDTO> itemDTOSet = new HashSet<>();

        cart.getCartItems().forEach(cartItem ->
                itemDTOSet.add(new GetCartItemDTO(cartItem.getProductName(),
                        cartItem.getProductSku(),
                        cartItem.getTotalPrice(),
                        cartItem.getQuantity()))
        );

        return itemDTOSet;
    }

    public CreateCartResponse createCart(CreateCartRequest request) {

        Cart cart = new Cart();
        cart.setUsername(request.getUsername());
        String reference = UUID.randomUUID().toString();
        cart.setReference(reference);
        cartRepository.save(cart);

        log.info("Created cart with reference:" + reference);

        return new CreateCartResponse(reference);
    }

    public void deleteCart(String reference) {

        Optional<Cart> cart0 = cartRepository.findByReference(reference);

        if (cart0.isPresent()) {
            cartRepository.delete(cart0.get());
        }
    }
}
