package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Order;
import com.pm.saaspossystem.model.OrderItem;
import com.pm.saaspossystem.model.Product;
import com.pm.saaspossystem.payload.dto.OrderItemDto;

public class OrderItemMapper {

    public static OrderItemDto toDto(OrderItem item) {

        if (item == null) {
            return null;
        }

        return OrderItemDto.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .orderId(item.getOrder() != null ? item.getOrder().getId() : null)
                .build();
    }

    public static OrderItem toEntity(OrderItemDto dto) {

        if (dto == null) {
            return null;
        }

        return OrderItem.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .product(
                        dto.getProductId() != null ?
                                Product.builder().id(dto.getProductId()).build() : null
                )
                .order(
                        dto.getOrderId() != null ?
                                Order.builder().id(dto.getOrderId()).build() : null
                )
                .build();
    }
}
