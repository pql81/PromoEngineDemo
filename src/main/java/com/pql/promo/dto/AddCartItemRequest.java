package com.pql.promo.dto;

import lombok.Data;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Data
public class AddCartItemRequest {

    private String sku;
    private Integer quantity;
}
