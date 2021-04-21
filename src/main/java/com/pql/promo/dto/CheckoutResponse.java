package com.pql.promo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@Data
public class CheckoutResponse {

    private final String username;
    private final String orderId;
    private final BigDecimal totalAmount;
    private final BigDecimal totalDiscount;
    private final BigDecimal amountDue;
}
