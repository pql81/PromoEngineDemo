package com.pql.promo.repository;

import com.pql.promo.domain.Product;
import com.pql.promo.domain.Promo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Repository
public interface PromoRepository extends CrudRepository<Promo, Long> {

    Optional<Promo> findByProducts(Product product);

    Optional<Promo> findByProductsAndPromoCode(Product product, String promoCode);
}
