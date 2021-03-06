package com.pql.promo.controller;

import com.pql.promo.dto.CheckoutResponse;
import com.pql.promo.dto.CreateCartRequest;
import com.pql.promo.dto.CreateCartResponse;
import com.pql.promo.dto.GetCartResponse;
import com.pql.promo.service.CartService;
import com.pql.promo.service.CheckoutService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Log4j2
@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CheckoutService checkoutService;

    @GetMapping("/carts/{reference}")
    public ResponseEntity<GetCartResponse> getCart(@PathVariable String reference) {
        log.info("GET carts/{" + reference + "}");

        GetCartResponse response = cartService.getCart(reference);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/carts")
    public ResponseEntity<CreateCartResponse> createCart(@RequestBody CreateCartRequest request) {
        log.info("POST carts " + request);

        CreateCartResponse response = cartService.createCart(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/carts/{reference}")
    public ResponseEntity<Void> deleteCart(@PathVariable String reference) {
        log.info("DELETE carts/{" + reference + "}");

        cartService.deleteCart(reference);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // As the purpose of this project is a demo for promotions,
    // the checkout process consist of just calculating and returning amounts
    // it returns anyway HttpStatus.CREATED as intended to be a creation call
    @PostMapping("/carts/{reference}/checkout")
    public ResponseEntity<CheckoutResponse> checkoutCart(@PathVariable String reference) {
        log.info("POST carts/{" + reference + "}/checkout");

        CheckoutResponse response = checkoutService.checkout(reference);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
