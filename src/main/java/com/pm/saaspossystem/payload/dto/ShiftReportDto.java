package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.model.PaymentSummery;
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

    private Long cashierId;
    private Long branchId;

    private List<PaymentSummery> paymentSummeries;


    private List<ProductDto> topSellingProducts;

    private List<OrderDto> recentOrders;


    private List<RefundDto> refunds;

}
