package com.pql.promo.repository;

import com.pql.promo.domain.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
