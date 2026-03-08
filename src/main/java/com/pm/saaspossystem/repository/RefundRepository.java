package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundRepository extends JpaRepository<Refund,Long> {
    List<Refund> findByCashierIdAndCreatedAtBetween(Long cashierId, LocalDateTime startDate, LocalDateTime endDate);

    List<Refund> findByShiftReportId(Long shiftReportId);

    List<Refund>  findByCashierId(Long cashierId);

    List<Refund>  findByBranchId(Long branchId);
}
