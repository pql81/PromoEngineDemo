package com.pql.promo.strategies;

import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Promo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Component("SINGLE_ITEM_FIXED")
public class FixedAmountNCartItemsPromo implements PromoStrategy {
    @Override
    public BigDecimal applyDiscount(CartItem cartItem) {
        int discount = 0;

        // make sure no promo has already applied to item
        if (!cartItem.isPromoApplied()) {
            Promo promo = cartItem.getProduct().getPromo();

            // check if the item quantity is enough for promo requirements
            if (promo.getNumItems() <= cartItem.getQuantity()) {
                discount = cartItem.getQuantity() / promo.getNumItems() * promo.getDiscountValue();

                // flag the item as promo has applied
                cartItem.setPromoApplied(true);
            }
        }

        return new BigDecimal(discount);
    }
}
