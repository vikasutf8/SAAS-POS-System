package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {

    private Long id;

    private Double totalAmount;

    private LocalDateTime createdAt;


    private BranchDto branch;
    private UserDto cashier;
    private CustomerDto customer;

    private  Long branchId;
    private Long cashierId; // userId
    private Long customerId;

    private PaymentType paymentType;

    private List<OrderItemDto> items;


}
