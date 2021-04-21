package com.pql.promo.strategies;

import com.pql.promo.domain.CartItem;

import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
public interface PromoStrategy {

    BigDecimal applyDiscount(CartItem cartItem);
}
