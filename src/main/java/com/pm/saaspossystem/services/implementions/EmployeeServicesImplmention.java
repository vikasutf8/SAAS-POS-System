package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.BranchRepository;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.EmployeeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServicesImplmention implements EmployeeServices {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createStoreEmployee(UserDto userDto, Long storeId) throws Exception {
        Store store =storeRepository.findById(storeId).orElseThrow(()->new Exception("store not found"));

 Branch branch =null;
        if(userDto.getRole() == UserRole.ROLE_BRANCH_MANAGER){
            if(userDto.getBranchId() ==null){
                throw  new Exception("Branch id is required");
            }

            branch = (branchRepository.findById(userDto.getBranchId()).orElseThrow(() -> new Exception("Branch not exist")));
        }
        User employee  = UserMapper.toEntity(userDto);
        employee.setStore(store);
        employee.setBranch(branch);
        employee.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User saveEmployee =userRepository.save(employee);
        if(employee.getRole() == UserRole.ROLE_BRANCH_MANAGER && branch != null){
            branch.setManager(saveEmployee);
            branchRepository.save(branch);
        }
return UserMapper.toDto(saveEmployee);
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
