package com.pm.saaspossystem.payload.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderItemDto {

    private Long id;

    private  Integer quantity;

    private Double price;

    private ProductDto product;
    private Long productId;

    private Long orderId ;
}
