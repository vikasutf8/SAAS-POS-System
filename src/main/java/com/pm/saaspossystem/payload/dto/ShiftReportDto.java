package com.pm.saaspossystem.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class ShiftReportDto {
    private Long id;

    private LocalDateTime shiftStart;

    private LocalDateTime shiftEnd;

    private Double totalSales;

    // total sales - refunds
    private Double netSales;

    private Integer totalOrders;


    private UserDto cashier;
    private Long cashierId;

    private List<RefundDto> refunds;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
