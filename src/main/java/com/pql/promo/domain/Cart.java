package com.pql.promo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by pasqualericupero on 20/04/2021.
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String reference;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItems;
}
