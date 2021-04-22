package com.pql.promo.controller;

import com.pql.promo.dto.GetProductResponse;
import com.pql.promo.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Log4j2
@RestController
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<List<GetProductResponse>> getProducts() {
        log.info("GET products");

        List<GetProductResponse> response = productService.listProducts();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products/{sku}")
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable String sku) {
        log.info("GET products/{" + sku + "}");

        GetProductResponse response = productService.getProduct(sku);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
