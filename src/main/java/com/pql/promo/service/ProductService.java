package com.pql.promo.service;

import com.pql.promo.domain.Product;
import com.pql.promo.dto.GetProductResponse;
import com.pql.promo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public GetProductResponse getProduct(String sku) {
        Optional<Product> product = productRepository.findBySku(sku);

        return product.isPresent() ? new GetProductResponse(product.get().getName(),
                                                            product.get().getSku(),
                                                            product.get().getPrice()) : null;
    }

    public List<GetProductResponse> listProducts() {
        Iterable<Product> products = productRepository.findAll();

        List<GetProductResponse> response = new ArrayList<>();

        for (Product product: products) {
            response.add(new GetProductResponse(product.getName(),
                                                product.getSku(),
                                                product.getPrice()));
        }

        return response;
    }
}
