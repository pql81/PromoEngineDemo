package com.pql.promo.dto;

import com.pql.promo.strategies.PromoType;
import lombok.Data;

/**
 * Created by pasqualericupero on 22/04/2021.
 */
@Data
public class CreatePromoResponse {

    private final String promoCode;
    private final PromoType promoType;
}
