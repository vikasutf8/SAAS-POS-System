package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.EmployeeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/employees")
public class EmployeeController {

    private final EmployeeServices employeeServices;

    // =========================================
    // CREATE STORE EMPLOYEE
    // =========================================
    @PostMapping("/store/{storeId}")
    public ResponseEntity<UserDto> createStoreEmployee(
            @RequestBody UserDto userDto,
            @PathVariable Long storeId) throws Exception {

        UserDto createdEmployee =
                employeeServices.createStoreEmployee(userDto, storeId);

        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    // =========================================
    // CREATE BRANCH EMPLOYEE
    // =========================================
    @PostMapping("/branch/{branchId}")
    public ResponseEntity<UserDto> createBranchEmployee(
            @RequestBody UserDto userDto,
            @PathVariable Long branchId) throws Exception {

        UserDto createdEmployee =
                employeeServices.createBranchEmployee(userDto, branchId);

        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    // =========================================
    // UPDATE EMPLOYEE
    // =========================================
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody UserDto userDto) throws Exception {

        UserDto updatedEmployee =
                employeeServices.updateEmployee(id, userDto);

        return ResponseEntity.ok(updatedEmployee);
    }

    // =========================================
    // DELETE EMPLOYEE
    // =========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id)
            throws UserExceptions {

        employeeServices.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================
    // GET STORE EMPLOYEES
    // =========================================
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserDto>> getStoreEmployees(
            @PathVariable Long storeId,
            @RequestParam(required = false) UserRole role) throws Exception {

        List<UserDto> employees =
                employeeServices.findAllStoreEmployees(storeId, role);

        return ResponseEntity.ok(employees);
    }

    // =========================================
    // GET BRANCH EMPLOYEES
    // =========================================
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<UserDto>> getBranchEmployees(
            @PathVariable Long branchId,
            @RequestParam(required = false) UserRole role) throws Exception {

        List<UserDto> employees =
                employeeServices.findAllBranchEmployees(branchId, role);

        return ResponseEntity.ok(employees);
    }
}
