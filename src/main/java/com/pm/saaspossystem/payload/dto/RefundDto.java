package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.PaymentType;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RefundDto {

    private Long id;
    private Long orderId;

    private String reason;

    private Double amount;

    private Long shiftReportId;

    private Long cashierId;

    private Long branchId;

    private PaymentType paymentType;

}
