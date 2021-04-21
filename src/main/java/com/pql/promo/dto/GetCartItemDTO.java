package com.pql.promo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Data
public class GetCartItemDTO {

    private final String name;
    private final String sku;
    private final BigDecimal price;
    private final Integer quantity;
}
