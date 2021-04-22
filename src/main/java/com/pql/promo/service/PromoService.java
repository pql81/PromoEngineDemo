package com.pql.promo.service;

import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import com.pql.promo.dto.CreatePromoRequest;
import com.pql.promo.dto.CreatePromoResponse;
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

    public GetPromoResponse getPromoForProduct(String sku) {
        Optional<Product> product0 = productRepository.findBySku(sku);

        if (!product0.isPresent()) {
            throw new ProductException("SKU does not exist!");
        }

        Optional<Promo> promo0 = promoRepository.findByProducts(product0.get());

        return promo0.isPresent() ? getPromoResponse(promo0.get()) : null;
    }

    public CreatePromoResponse createPromoForProduct(String sku, CreatePromoRequest request) {
        Promo promo = new Promo();
        promo.setName(request.getName());
        promo.setPromoCode(request.getPromoCode());
        promo.setPromoType(request.getPromoType());
        promo.setDiscountValue(request.getDiscountVal());
        promo.setNumItems(request.getNumItems());
        promoRepository.save(promo);

        Product product = findProductWithNoPromo(sku);
        product.setPromo(promo);
        productRepository.save(product);

        if (request.getSkus() != null && !request.getSkus().isEmpty()) {
            for (String sku0: request.getSkus()) {
                Product multiProduct = findProductWithNoPromo(sku0);

                multiProduct.setPromo(promo);
                productRepository.save(multiProduct);
            }
        }

        return new CreatePromoResponse(promo.getPromoCode(), promo.getPromoType());
    }

    private Product findProductWithNoPromo(String sku) {
        Optional<Product> product0 = productRepository.findBySku(sku);

        if (!product0.isPresent()) {
            throw new ProductException("SKU does not exist!");
        }

        Optional<Promo> promo0 = promoRepository.findByProducts(product0.get());

        if (promo0.isPresent()) {
            throw new ProductException("Product has already a promo!");
        }

        return product0.get();
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
