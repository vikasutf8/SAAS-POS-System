package com.pm.saaspossystem.services;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.UserDto;

import java.util.List;

public interface EmployeeServices {
    UserDto createStoreEmployee(UserDto userDto,Long storeId) throws Exception;
    UserDto createBranchEmployee(UserDto userDto,Long branchId) throws Exception;
    UserDto updateEmployee(Long id, UserDto userDto) throws Exception;
    void deleteEmployee(Long id) throws UserExceptions;
    List<UserDto> findAllStoreEmployees(Long storeId, UserRole userRole) throws Exception;
    List<UserDto> findAllBranchEmployees(Long branchId, UserRole userRole) throws Exception;

}
