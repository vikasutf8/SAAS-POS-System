package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items",
    indexes = {
        @Index(name = "idx_order_item_product_id", columnList = "product_id"),
        @Index(name = "idx_order_item_order_id", columnList = "order_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  Integer quantity;

    private Double price;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order ;

}
