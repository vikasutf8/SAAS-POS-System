package com.pm.saaspossystem.services;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.payload.dto.UserDto;

import java.util.List;

public interface EmployeeServices {
    UserDto createStoreEmployee(UserDto userDto,Long storeId);
    UserDto createBranchEmployee(UserDto userDto,Long branchId);
    UserDto updateEmployee(Long id, UserDto userDto);
    void deleteEmployee(Long id);
    List<UserDto> findAllStoreEmployees(Long storeId, UserRole userRole);
    List<UserDto> findAllBranchEmployees(Long branchId, UserRole userRole);

}
