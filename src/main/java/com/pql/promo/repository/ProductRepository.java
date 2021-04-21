package com.pql.promo.repository;

import com.pql.promo.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findBySku(String sku);
}
