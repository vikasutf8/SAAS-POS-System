package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
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
      private Product product;
    @ManyToOne
    private Order order ;

}
