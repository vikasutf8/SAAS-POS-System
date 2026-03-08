package com.pm.saaspossystem.services;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.ShiftReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftReportService {

    ShiftReportDto startShift(Long cashierId, Long branchId, LocalDateTime shiftStartTime) throws Exception;
    ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEndTime) throws Exception;
    ShiftReportDto getShiftReportById(Long Id);

    List<ShiftReportDto> getAllShiftReport();
    List<ShiftReportDto> getShiftReportByBranchId(Long branchId);
    List<ShiftReportDto> getShiftReportByCashierId(Long Id);

// progress or curretn shift report for a cashier
    ShiftReportDto getCurrentShiftProgress(Long cashierId) throws UserExceptions;

    //shift report of specific date

    ShiftReportDto getShiftReportByCashierIdAndDate(Long cashierId, LocalDateTime date);
}
