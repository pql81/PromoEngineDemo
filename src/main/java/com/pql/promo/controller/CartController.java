package com.pql.promo.controller;

import com.pql.promo.dto.CheckoutResponse;
import com.pql.promo.dto.CreateCartRequest;
import com.pql.promo.dto.CreateCartResponse;
import com.pql.promo.dto.GetCartResponse;
import com.pql.promo.service.CartService;
import com.pql.promo.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CheckoutService checkoutService;

    @GetMapping("/carts/{reference}")
    public ResponseEntity<GetCartResponse> getCart(@PathVariable String reference) {

        GetCartResponse response = cartService.getCart(reference);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/carts")
    public ResponseEntity<CreateCartResponse> createCart(@RequestBody CreateCartRequest request) {

        CreateCartResponse response = cartService.createCart(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{reference}")
    public ResponseEntity<Void> deleteCart(@PathVariable String reference) {

        cartService.deleteCart(reference);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // As the purpose of this project is a demo for promotions,
    // the checkout process consist of just calculating and returning amounts
    @PostMapping("/carts/{reference}/checkout")
    public ResponseEntity<CheckoutResponse> checkoutCart(@PathVariable String reference) {

        CheckoutResponse response = checkoutService.checkout(reference);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
