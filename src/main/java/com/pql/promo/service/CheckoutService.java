package com.pql.promo.service;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.dto.CheckoutResponse;
import com.pql.promo.exception.CartException;
import com.pql.promo.repository.CartRepository;
import com.pql.promo.strategies.PromoStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Log4j2
@Service
public class CheckoutService {

    @Autowired
    CartRepository cartRepository;

    private final Map<String, PromoStrategy> promoStrategyMap;


    public CheckoutService(Map<String, PromoStrategy> promoStrategyMap) {
        this.promoStrategyMap = promoStrategyMap;
    }

    public CheckoutResponse checkout(String reference) {
        Optional<Cart> cart0 = cartRepository.findByReference(reference);

        if (!cart0.isPresent()) {
            log.warn("Cart " + reference + " does not exist!");
            throw new CartException("Cart does not exist!");
        }

        Cart cart = cart0.get();
        BigDecimal totalAmount = new BigDecimal(0);
        BigDecimal totalDiscount = new BigDecimal(0);

        for (CartItem cartItem: cart.getCartItems()) {
            // add to to total amount
            totalAmount = totalAmount.add(cartItem.getTotalPrice());
            totalDiscount = totalDiscount.add(getCartItemDiscount(cartItem));
        }

        String orderId = UUID.randomUUID().toString();

        return new CheckoutResponse(cart.getUsername(), orderId, totalAmount, totalDiscount, totalAmount.subtract(totalDiscount));
    }

    private BigDecimal getCartItemDiscount(CartItem cartItem) {
        BigDecimal discount = BigDecimal.ZERO;

        // if promo exist then apply it
        if (cartItem.getProduct().getPromo() != null) {
            log.info("A promo applies to product: " + cartItem.getProduct().getSku());

            // select the right prom strategy for the item
            PromoStrategy promoStrategy = promoStrategyMap.get(cartItem.getProduct().getPromo().getPromoType().name());
            discount = promoStrategy.applyDiscount(cartItem);
        }

        return discount;
    }
}
