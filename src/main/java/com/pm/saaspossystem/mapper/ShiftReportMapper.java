package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.ShiftReport;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.ShiftReportDto;

import java.util.List;
import java.util.stream.Collectors;

public class ShiftReportMapper {

    // Entity -> DTO
    public static ShiftReportDto toDto(ShiftReport shiftReport) {

        if (shiftReport == null) {
            return null;
        }

        return ShiftReportDto.builder()
                .id(shiftReport.getId())
                .shiftStart(shiftReport.getShiftStart())
                .shiftEnd(shiftReport.getShiftEnd())
                .totalSales(shiftReport.getTotalSales())
                .netSales(shiftReport.getNetSales())
                .totalOrders(shiftReport.getTotalOrders())

                .cashierId(shiftReport.getCashier() != null ? shiftReport.getCashier().getId() : null)
                .branchId(shiftReport.getBranch() != null ? shiftReport.getBranch().getId() : null)
                .paymentSummeries(shiftReport.getPaymentSummeries())

                .topSellingProducts(
                        shiftReport.getTopSellingProducts() != null ?
                                shiftReport.getTopSellingProducts()
                                        .stream()
                                        .map(ProductMapper::toDto)
                                        .collect(Collectors.toList())
                                : null
                )

                .recentOrders(
                        shiftReport.getRecentOrders() != null ?
                                shiftReport.getRecentOrders()
                                        .stream()
                                        .map(OrderMapper::toDto)
                                        .collect(Collectors.toList())
                                : null
                )

                .refunds(
                        shiftReport.getRefunds() != null ?
                                shiftReport.getRefunds()
                                        .stream()
                                        .map(RefundMapper::toDto)
                                        .collect(Collectors.toList())
                                : null
                )



                .build();
    }


    // DTO -> Entity
    public static ShiftReport toEntity(ShiftReportDto dto) {

        if (dto == null) {
            return null;
        }

        return ShiftReport.builder()
                .id(dto.getId())
                .shiftStart(dto.getShiftStart())
                .shiftEnd(dto.getShiftEnd())
                .totalSales(dto.getTotalSales())
                .netSales(dto.getNetSales())
                .totalOrders(dto.getTotalOrders())

                .cashier(dto.getCashierId() != null ?
                        User.builder().id(dto.getCashierId()).build() : null)

                .branch(dto.getBranchId() != null ?
                        Branch.builder().id(dto.getBranchId()).build() : null)

                .paymentSummeries(dto.getPaymentSummeries())

//                .topSellingProducts(
//                        dto.getTopSellingProducts() != null ?
//                                dto.getTopSellingProducts()
//                                        .stream()
//                                        .map(ProductMapper::toEntity)
//                                        .collect(Collectors.toList())
//                                : null
//                )

                .recentOrders(
                        dto.getRecentOrders() != null ?
                                dto.getRecentOrders()
                                        .stream()
                                        .map(OrderMapper::toEntity)
                                        .collect(Collectors.toList())
                                : null
                )

                .refunds(
                        dto.getRefunds() != null ?
                                dto.getRefunds()
                                        .stream()
                                        .map(RefundMapper::toEntity)
                                        .collect(Collectors.toList())
                                : null
                )

                .build();
    }


    // List Mapper
    public static List<ShiftReportDto> toDtoList(List<ShiftReport> shiftReports) {

        return shiftReports.stream()
                .map(ShiftReportMapper::toDto)
                .collect(Collectors.toList());
    }
}
