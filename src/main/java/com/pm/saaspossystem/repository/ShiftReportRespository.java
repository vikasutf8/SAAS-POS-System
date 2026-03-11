package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.model.ShiftReport;
import com.pm.saaspossystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftReportRespository extends JpaRepository<ShiftReport,Long> {
    List<ShiftReport> findByBranchId(Long branchId);

    List<ShiftReport> findByCashierId(Long cashierId);

//    Optional<ShiftReport> findByCashierIdAndShiftEndIsNull(Long cashierId);

    Optional<ShiftReport> findByCashierIdAndShiftStartBetween(
            Long cashierId,
            LocalDateTime start,
            LocalDateTime end
    );

//    ShiftReport findByCashierIdAndShiftEndTimeIsNull();

    Optional<ShiftReport> findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(User cashier);
}
