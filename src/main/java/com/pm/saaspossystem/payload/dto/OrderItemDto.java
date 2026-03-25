package com.pm.saaspossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private  Integer quantity;

    private Double price;

    private ProductDto product;
    private Long productId;

    private Long orderId ;
}
