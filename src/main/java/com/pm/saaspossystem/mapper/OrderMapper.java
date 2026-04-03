package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.*;
import com.pm.saaspossystem.payload.dto.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toDto(Order order) {

        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())

                .branchId(order.getBranch() != null ? order.getBranch().getId() : null)
                .cashierId(order.getCashier() != null ? order.getCashier().getId() : null)
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .paymentType(order.getPaymentType())
                .orderStatus(order.getOrderStatus())
                .items(order.getItems() != null ?
                        order.getItems().stream()
                                .map(OrderItemMapper::toDto)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }

    public static Order toEntity(OrderDto dto) {

        if (dto == null) {
            return null;
        }

        Order order = Order.builder()
                .id(dto.getId())
                .totalAmount(dto.getTotalAmount())

                .paymentType(dto.getPaymentType())
                .orderStatus(dto.getOrderStatus())
                .branch(
                        dto.getBranchId() != null ?
                                Branch.builder().id(dto.getBranchId()).build() : null
                )
                .cashier(
                        dto.getCashierId() != null ?
                                User.builder().id(dto.getCashierId()).build() : null
                )
                .customer(
                        dto.getCustomerId() != null ?
                                Customer.builder().id(dto.getCustomerId()).build() : null
                )
                .build();

        if (dto.getItems() != null) {
            List<OrderItem> items = dto.getItems()
                    .stream()
                    .map(OrderItemMapper::toEntity)
                    .peek(item -> item.setOrder(order))
                    .collect(Collectors.toList());

            order.setItems(items);
        }

        return order;
    }
}
