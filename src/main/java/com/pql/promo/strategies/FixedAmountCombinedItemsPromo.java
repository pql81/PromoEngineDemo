package com.pql.promo.strategies;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Component("MULTIPLE_ITEMS_FIXED")
public class FixedAmountCombinedItemsPromo implements PromoStrategy {
    @Override
    public BigDecimal applyDiscount(CartItem cartItem) {
        int discount = 0;

        // make sure no promo has already applied to item
        if (!cartItem.isPromoApplied()) {
            Promo promo = cartItem.getProduct().getPromo();

            // check if there are all items matching promo requirements
            Set<Product> promoProducts = promo.getProducts();
            Cart cart = cartItem.getCart();

            Map<String, CartItem> cartProductMap = cart.getCartItems().stream().collect(Collectors.toMap(CartItem::getProductSku, Function.identity()));

            // populate a list of cartItem that match the promo criteria
            List<CartItem> promoCartItems = new ArrayList<>();

            for (Product product: promoProducts) {
                if (cartProductMap.containsKey(product.getSku())) {
                    CartItem promoItem = cartProductMap.get(product.getSku());

                    // if one of the item required for the promo has already applied for other promo the skip
                    if (promoItem.isPromoApplied()) {
                        promoCartItems.clear();
                        break;
                    } else {
                        promoCartItems.add(cartProductMap.get(product.getSku()));
                    }
                } else {
                    // no match for promo - clear list end exit
                    promoCartItems.clear();
                    break;
                }
            }


            if (!promoCartItems.isEmpty()) {
                // calculate how many times the discount can apply based on the min quantity between eligible products
                Optional<CartItem> minQty = promoCartItems.stream().min(Comparator.comparing(CartItem::getQuantity));
                int maxDiscountTimes = minQty.get().getQuantity();

                // set all required items as promo applied
                for (CartItem promoCartItem : promoCartItems) {
                    promoCartItem.setPromoApplied(true);
                }

                discount = maxDiscountTimes * promo.getDiscountValue();
            }
        }

        return new BigDecimal(discount);
    }
}
