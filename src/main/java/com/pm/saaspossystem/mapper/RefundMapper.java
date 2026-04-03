package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.*;
import com.pm.saaspossystem.payload.dto.RefundDto;

public class RefundMapper {

    // Entity -> DTO
    public static RefundDto toDto(Refund refund) {

        if (refund == null) {
            return null;
        }

        return RefundDto.builder()
                .id(refund.getId())
                .reason(refund.getReason())
                .amount(refund.getAmount())
                .paymentType(refund.getPaymentType())
                .orderId(refund.getOrder() != null ? refund.getOrder().getId() : null)
                .shiftReportId(refund.getShiftReport() != null ? refund.getShiftReport().getId() : null)
                .cashierId(refund.getCashier() != null ? refund.getCashier().getId() : null)
                .branchId(refund.getBranch() != null ? refund.getBranch().getId() : null)
                .build();
    }


    // DTO -> Entity
    public static Refund toEntity(RefundDto dto) {

        if (dto == null) {
            return null;
        }

        return Refund.builder()
                .id(dto.getId())
                .reason(dto.getReason())
                .amount(dto.getAmount())
                .paymentType(dto.getPaymentType())


                .order(dto.getOrderId() != null
                        ? Order.builder().id(dto.getOrderId()).build()
                        : null)

                .shiftReport(dto.getShiftReportId() != null
                        ? ShiftReport.builder().id(dto.getShiftReportId()).build()
                        : null)

                .cashier(dto.getCashierId() != null
                        ? User.builder().id(dto.getCashierId()).build()
                        : null)

                .branch(dto.getBranchId() != null
                        ? Branch.builder().id(dto.getBranchId()).build()
                        : null)

                .build();
    }
}
