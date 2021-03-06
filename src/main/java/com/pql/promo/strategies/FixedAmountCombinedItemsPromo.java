package com.pql.promo.strategies;

import com.pql.promo.domain.Cart;
import com.pql.promo.domain.CartItem;
import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Log4j2
@Component("MULTIPLE_ITEMS_FIXED")
public class FixedAmountCombinedItemsPromo implements PromoStrategy {
    @Override
    public BigDecimal applyDiscount(CartItem cartItem) {
        log.info("Apply MULTIPLE_ITEMS_FIXED strategy to cart item");

        BigDecimal discount = BigDecimal.ZERO;

        // make sure no promo has already applied to item
        if (!cartItem.isPromoApplied()) {
            log.info("Cart item has not applied to any promo yet!");

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
                        log.info("Some other required item has already applied to a promo - quit");
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
                BigDecimal maxDiscountTimes = new BigDecimal(minQty.get().getQuantity());
                BigDecimal beforeDiscount = BigDecimal.ZERO;

                // set all required items as promo applied
                for (CartItem promoCartItem : promoCartItems) {
                    promoCartItem.setPromoApplied(true);

                    // calculate the total amount of items eligible for promo before discount applies
                    beforeDiscount = beforeDiscount.add(promoCartItem.getProduct().getPrice());
                }

                log.info("Amount before discount: " + beforeDiscount);
                log.info("Discounted amount " + promo.getDiscountValue());

                discount = (beforeDiscount.subtract(new BigDecimal(promo.getDiscountValue())).multiply(maxDiscountTimes));
            }
        }

        return discount;
    }
}
