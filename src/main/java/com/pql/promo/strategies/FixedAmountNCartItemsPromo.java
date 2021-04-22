package com.pql.promo.strategies;

import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Promo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Log4j2
@Component("SINGLE_ITEM_FIXED")
public class FixedAmountNCartItemsPromo implements PromoStrategy {
    @Override
    public BigDecimal applyDiscount(CartItem cartItem) {
        log.info("Apply SINGLE_ITEM_FIXED strategy to cart item");

        BigDecimal discount = BigDecimal.ZERO;

        // make sure no promo has already applied to item
        if (!cartItem.isPromoApplied()) {
            log.info("Cart item has not applied to any promo yet!");

            Promo promo = cartItem.getProduct().getPromo();

            // check if the item quantity is enough for promo requirements
            if (promo.getNumItems() <= cartItem.getQuantity()) {
                // how many times can the same promo applies
                int timesToApply = cartItem.getQuantity() / promo.getNumItems();

                // calculate the total amount of items eligible for promo before discount applies
                BigDecimal beforeDiscount = cartItem.getProduct().getPrice().multiply(new BigDecimal(timesToApply * promo.getNumItems()));

                // discounted amount is the new calculated price to pay for eligible items
                int discountedAmount = cartItem.getQuantity() / promo.getNumItems() * promo.getDiscountValue();

                log.info("Amount before discount: " + beforeDiscount);
                log.info("Discounted amount " + discountedAmount);

                discount = beforeDiscount.subtract(new BigDecimal(discountedAmount));

                // flag the item as promo has applied
                cartItem.setPromoApplied(true);
            }
        }

        return discount;
    }
}
