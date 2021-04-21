package com.pql.promo.repository;

import com.pql.promo.domain.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {

    Optional<Cart> findByReference(String cartReference);
}
