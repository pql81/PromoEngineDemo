package com.pql.promo.service;

import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.dto.GetPromoResponse;
import com.pql.promo.exception.ProductException;
import com.pql.promo.repository.ProductRepository;
import com.pql.promo.repository.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Service
public class PromoService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PromoRepository promoRepository;

    public GetPromoResponse getPromoForProduct(String sku, String code) {
        Optional<Product> product0 = productRepository.findBySku(sku);

        if (!product0.isPresent()) {
            throw new ProductException("SKU does not exist!");
        }

        Optional<Promo> promo0 = promoRepository.findByProductsAndPromoCode(product0.get(), code);

        return promo0.isPresent() ? getPromoResponse(promo0.get()) : null;
    }

    private GetPromoResponse getPromoResponse(Promo promo) {
        GetPromoResponse response = new GetPromoResponse(promo.getName(), promo.getPromoCode(), promo.getPromoType().name());

        Set<String> skuSet = new HashSet<>();

        promo.getProducts().forEach(product ->
                skuSet.add(product.getSku())
        );

        response.setSkus(skuSet);

        return response;
    }
}
