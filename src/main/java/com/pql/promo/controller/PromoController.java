package com.pql.promo.controller;

import com.pql.promo.dto.CreatePromoRequest;
import com.pql.promo.dto.CreatePromoResponse;
import com.pql.promo.dto.GetPromoResponse;
import com.pql.promo.service.PromoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Log4j2
@RestController
@RequestMapping("/products")
public class PromoController {

    @Autowired
    PromoService promoService;

    @GetMapping("/{sku}/promos")
    public ResponseEntity<GetPromoResponse> getPromo(@PathVariable String sku) {
        log.info("GET products/{" + sku + "}/promos");

        GetPromoResponse response = promoService.getPromoForProduct(sku);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promo not found");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{sku}/promos")
    public ResponseEntity<CreatePromoResponse> createPromo(@PathVariable String sku, @RequestBody CreatePromoRequest request) {
        log.info("POST products/{" + sku + "}/promos " + request);

        CreatePromoResponse response = promoService.createPromoForProduct(sku, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
