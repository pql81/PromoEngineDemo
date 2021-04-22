package com.pql.promo.service;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Product;
import com.pql.promo.dto.AddCartItemRequest;
import com.pql.promo.dto.AddCartItemResponse;
import com.pql.promo.exception.CartException;
import com.pql.promo.exception.InsufficientQuantityException;
import com.pql.promo.exception.ProductException;
import com.pql.promo.repository.CartItemRepository;
import com.pql.promo.repository.CartRepository;
import com.pql.promo.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Log4j2
@Service
public class CartItemService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public AddCartItemResponse addItemToCart(String reference, AddCartItemRequest request) {

        Optional<Cart> cart0 = cartRepository.findByReference(reference);

        if (!cart0.isPresent()) {
            log.warn("Cart does not exist:" + reference);
            throw new CartException("Cart does not exist!");
        }

        Optional<Product> product0 = productRepository.findBySku(request.getSku());

        if (!product0.isPresent()) {
            log.warn("SKU does not exist:" + request.getSku());
            throw new ProductException("SKU does not exist!");
        }

        Cart cart = cart0.get();
        CartItem cartItem = getItemAlreadyInCart(cart, product0.get());

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product0.get());
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem.incrementQuantity(request.getQuantity());
        }

        saveCartItem(cart, cartItem);

        return new AddCartItemResponse(cartItem.getProductName(), cartItem.getTotalPrice());
    }

    public void removeItemfromCart(String reference, String sku) {

        Optional<Cart> cart0 = cartRepository.findByReference(reference);

        if (!cart0.isPresent()) {
            log.warn("Cart does not exist:" + reference);
            throw new CartException("Cart does not exist!");
        }

        Optional<Product> product0 = productRepository.findBySku(sku);

        if (!product0.isPresent()) {
            log.warn("SKU does not exist:" + sku);
            throw new ProductException("SKU does not exist!");
        }

        Cart cart = cart0.get();
        CartItem cartItem = getItemAlreadyInCart(cart, product0.get());

        if (cartItem == null) {
            throw new InsufficientQuantityException("Product not found in cart");
        }

        deleteCartItem(cart, cartItem);
    }

    public void saveCartItem(Cart cart, CartItem cartItem) {
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    public void deleteCartItem(Cart cart, CartItem cartItem) {
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    private CartItem getItemAlreadyInCart(Cart cart, Product product) {
        for (CartItem existingItem: cart.getCartItems()) {
            if (existingItem.getProduct().getId().equals(product.getId())) {
                return existingItem;
            }
        }
        return null;
    }
}
