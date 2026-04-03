package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.OrderStatus;
import com.pm.saaspossystem.domain.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private Double totalAmount;

    private  Long branchId;
    private Long cashierId; // userId
    private Long customerId;

    private PaymentType paymentType;

    private OrderStatus orderStatus;

    private List<OrderItemDto> items;


}
