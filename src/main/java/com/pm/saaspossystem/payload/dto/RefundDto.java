package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class RefundDto {

    private Long id;

    private OrderDto order;
    private Long orderId;

    private String reason;

    private Double amount;

    private ShiftReportDto shiftReport;
    private Long shiftReportId;


    private UserDto cashier;
    private Long cashierId;

    private BranchDto branch;
    private Long branchId;

    private PaymentType paymentType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
