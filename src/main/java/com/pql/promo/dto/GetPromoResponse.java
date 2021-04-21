package com.pql.promo.dto;

import lombok.Data;

import java.util.Set;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Data
public class GetPromoResponse {

    private final String name;
    private final String promoCode;
    private final String promoType;

    private Set<String> skus;
}
