package com.pql.promo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="promo_id")
    private Promo promo;

}
