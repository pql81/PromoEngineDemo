package com.pql.promo.controller;

import com.pql.promo.dto.GetPromoResponse;
import com.pql.promo.service.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@RestController
@RequestMapping("/products")
public class PromoController {

    @Autowired
    PromoService promoService;

    @GetMapping("/{sku}/promos/{code}")
    public ResponseEntity<GetPromoResponse> getPromo(@PathVariable String sku, @PathVariable String code) {

        GetPromoResponse response = promoService.getPromoForProduct(sku, code);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promo not found");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
