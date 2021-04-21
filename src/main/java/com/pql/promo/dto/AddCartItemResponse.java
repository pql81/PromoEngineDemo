package com.pql.promo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Data
public class AddCartItemResponse {

    private final String name;
    private final BigDecimal price;
}
