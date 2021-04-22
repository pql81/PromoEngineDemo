package com.pql.promo.domain;

import com.pql.promo.strategies.PromoType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="promotions")
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String promoCode;

    @Enumerated(EnumType.STRING)
    private PromoType promoType;

    // it can be either a fixed amount or a percent off
    private Integer discountValue;

    private Integer numItems;

    @OneToMany(mappedBy = "promo", fetch = FetchType.EAGER)
    private Set<Product> products;
}
