package com.pql.promo.dto;

import com.pql.promo.strategies.PromoType;
import lombok.Data;

import java.util.Set;

/**
 * Created by pasqualericupero on 22/04/2021.
 */
@Data
public class CreatePromoRequest {

    private String name;
    private String promoCode;
    private PromoType promoType;
    private Integer discountVal;
    private Integer numItems;

    private Set<String> skus;
}
