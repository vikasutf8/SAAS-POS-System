package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.ShiftReportDto;
import com.pm.saaspossystem.services.ShiftReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/shift_reports")
public class ShiftReportController {

    private final ShiftReportService shiftReportService;

    // Start Shift
    @PostMapping("/start")
    public ResponseEntity<ShiftReportDto> startShift(
            @RequestParam Long cashierId,
            @RequestParam Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime shiftStartTime
    ) throws Exception {

        ShiftReportDto shiftReport =
                shiftReportService.startShift(cashierId, branchId, shiftStartTime);

        return ResponseEntity.ok(shiftReport);
    }

    // End Shift
    @PutMapping("/end/{shiftReportId}")
    public ResponseEntity<ShiftReportDto> endShift(
            @PathVariable Long shiftReportId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime shiftEndTime
    ) throws Exception {

        ShiftReportDto shiftReport =
                shiftReportService.endShift(shiftReportId, shiftEndTime);

        return ResponseEntity.ok(shiftReport);
    }

    // Get by Id
    @GetMapping("/{id}")
    public ResponseEntity<ShiftReportDto> getShiftReportById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                shiftReportService.getShiftReportById(id)
        );
    }

    // Get all reports
    @GetMapping
    public ResponseEntity<List<ShiftReportDto>> getAllShiftReports() {

        return ResponseEntity.ok(
                shiftReportService.getAllShiftReport()
        );
    }

    // Get reports by branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<ShiftReportDto>> getShiftReportsByBranch(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                shiftReportService.getShiftReportByBranchId(branchId)
        );
    }

    // Get reports by cashier
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<ShiftReportDto>> getShiftReportsByCashier(
            @PathVariable Long cashierId) {

        return ResponseEntity.ok(
                shiftReportService.getShiftReportByCashierId(cashierId)
        );
    }

    // Current Shift Progress
    @GetMapping("/current/{cashierId}")
    public ResponseEntity<ShiftReportDto> getCurrentShiftProgress(
            @PathVariable Long cashierId) throws UserExceptions {

        return ResponseEntity.ok(
                shiftReportService.getCurrentShiftProgress(cashierId)
        );
    }

    // Get Shift Report By Date
    @GetMapping("/cashier/{cashierId}/date")
    public ResponseEntity<ShiftReportDto> getShiftReportByDate(
            @PathVariable Long cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date
    ) {

        return ResponseEntity.ok(
                shiftReportService.getShiftReportByCashierIdAndDate(cashierId, date)
        );
    }
}
