package com.pm.saaspossystem.services;

import com.pm.saaspossystem.payload.dto.RefundDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {

    RefundDto createRefund(RefundDto refundDto) throws Exception;
    List<RefundDto> getAllRefunds();
    List<RefundDto>  getRefundByCashier(Long cashierId);

    List<RefundDto>  getRefundByShiftReport(Long shiftReportId);

    List<RefundDto> getRefundByCashierAndDateRange(
            Long cashierId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<RefundDto> getRefundByBranch(Long branchId);
    RefundDto getRefundById(Long refundId);

    void deleteRefund(Long refundId); //only super admin did it



}
