package com.pql.promo.controller;

import com.pql.promo.dto.AddCartItemRequest;
import com.pql.promo.dto.AddCartItemResponse;
import com.pql.promo.service.CartItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Log4j2
@RestController
@RequestMapping("/carts")
public class CartItemController {

    @Autowired
    CartItemService cartItemService;

    @PostMapping("/{reference}/items")
    public ResponseEntity<AddCartItemResponse> addCartItem(@PathVariable String reference, @RequestBody AddCartItemRequest request) {
        log.info("POST carts/{" + reference + "}/items " + request);

        AddCartItemResponse response = cartItemService.addItemToCart(reference, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{reference}/items/{sku}")
    public ResponseEntity<Void> removeCartItem(@PathVariable String reference, @PathVariable String sku) {
        log.info("DELETE carts/{" + reference + "}/items/{" + sku + "}");

        cartItemService.removeItemfromCart(reference, sku);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
