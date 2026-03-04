package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.BranchRepository;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.EmployeeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServicesImplmention implements EmployeeServices {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;

    @Override
    public UserDto createStoreEmployee(UserDto userDto, Long storeId) {
        return null;
    }

    @Override
    public UserDto createBranchEmployee(UserDto userDto, Long branchId) {
        return null;
    }

    @Override
    public UserDto updateEmployee(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public void deleteEmployee(Long id) {

    }

    @Override
    public List<UserDto> findAllStoreEmployees(Long storeId, UserRole userRole) {
        return List.of();
    }

    @Override
    public List<UserDto> findAllBranchEmployees(Long branchId, UserRole userRole) {
        return List.of();
    }
}
