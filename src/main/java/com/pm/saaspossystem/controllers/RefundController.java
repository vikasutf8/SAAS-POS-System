package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.payload.dto.RefundDto;
import com.pm.saaspossystem.services.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/refund")
public class RefundController {

    private final RefundService refundService;

    // Create Refund
    @PostMapping
    public RefundDto createRefund(@RequestBody RefundDto refundDto) throws Exception {
        return refundService.createRefund(refundDto);
    }

    // Get All Refunds
    @GetMapping
    public List<RefundDto> getAllRefunds() {
        return refundService.getAllRefunds();
    }

    // Get Refund By Id
    @GetMapping("/{refundId}")
    public RefundDto getRefundById(@PathVariable Long refundId) {
        return refundService.getRefundById(refundId);
    }

    // Get Refunds By Cashier
    @GetMapping("/cashier/{cashierId}")
    public List<RefundDto> getRefundByCashier(@PathVariable Long cashierId) {
        return refundService.getRefundByCashier(cashierId);
    }

    // Get Refunds By Shift Report
    @GetMapping("/shift/{shiftReportId}")
    public List<RefundDto> getRefundByShiftReport(@PathVariable Long shiftReportId) {
        return refundService.getRefundByShiftReport(shiftReportId);
    }

    // Get Refunds By Branch
    @GetMapping("/branch/{branchId}")
    public List<RefundDto> getRefundByBranch(@PathVariable Long branchId) {
        return refundService.getRefundByBranch(branchId);
    }

    // Get Refunds By Cashier and Date Range
    @GetMapping("/cashier/{cashierId}/range")
    public List<RefundDto> getRefundByCashierAndDateRange(
            @PathVariable Long cashierId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        return refundService.getRefundByCashierAndDateRange(cashierId, startDate, endDate);
    }

    // Delete Refund (Super Admin)
    @DeleteMapping("/{refundId}")
    public void deleteRefund(@PathVariable Long refundId) {
        refundService.deleteRefund(refundId);
    }

}
