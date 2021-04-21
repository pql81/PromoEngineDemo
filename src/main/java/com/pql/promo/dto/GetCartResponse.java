package com.pql.promo.dto;

import lombok.Data;

import java.util.Set;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Data
public class GetCartResponse {

    private final String username;
    private final String reference;

    private Set<GetCartItemDTO> cartItems;
}
