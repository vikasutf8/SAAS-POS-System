package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.exceptions.UserExceptions;
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
            branch.setBranchManager(saveEmployee);
            branch.setStore(store);
            branchRepository.save(branch);
        }
        return UserMapper.toDto(saveEmployee);


    }

    @Override
    public UserDto createBranchEmployee(UserDto userDto, Long branchId) throws Exception {
        Branch branch =branchRepository.findById(branchId).orElseThrow(()->new Exception("Branch not found"));

        User user = UserMapper.toEntity(userDto);
        user.setBranch(branch);
        user.setStore(branch.getStore());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateEmployee(Long id, UserDto userDto) throws Exception {
        User existingEmployee =userRepository.findById(id).orElseThrow(()->new UserExceptions("Employee not found"));

        Branch branch = branchRepository.findById(userDto.getBranchId()).orElseThrow(()->new Exception("Branch not found"));
        Store store =storeRepository.findById(userDto.getStoreId()).orElseThrow(()->new Exception("Store not found"));

        existingEmployee.setBranch(branch);
        existingEmployee.setStore(store);

        UserMapper.updateEntity(existingEmployee,userDto);

        User updateEmployeeDetails=userRepository.save(existingEmployee);

        return UserMapper.toDto(updateEmployeeDetails);
    }

    @Override
    public void deleteEmployee(Long id) throws UserExceptions {
        User existingEmployee =userRepository.findById(id).orElseThrow(()->new UserExceptions("Employee not found"));

//        Branch branch = branchRepository.findById(userDto.getBranchId()).orElseThrow(()->new Exception("Branch not found"));
//        Store store =storeRepository.findById(userDto.getStoreId()).orElseThrow(()->new Exception("Store not found"));
        userRepository.delete(existingEmployee);
    }

    @Override
    public List<UserDto> findAllStoreEmployees(Long storeId, UserRole userRole) throws Exception {
        Store store =storeRepository.findById(storeId).orElseThrow(()->new Exception("store not found"));
//        List<User> users =userRepository.findByStoreAndRoleIn(store,List.of(
//                UserRole.ROLE_STORE_MANAGER,UserRole.ROLE_BRANCH_MANAGER
//        ));

        List<User> users = userRepository.findByStore(store);

        return users.stream().filter(user -> user.getRole() == userRole).map(UserMapper::toDto).toList();

    }

    @Override
    public List<UserDto> findAllBranchEmployees(Long branchId, UserRole userRole) throws Exception {
        Branch branch = branchRepository.findById(branchId).orElseThrow(()->new Exception("Branch not found"));

        List<User> users = userRepository.findByBranchId(branchId);

        return users.stream().filter(user -> user.getRole() == userRole).map(UserMapper::toDto).toList();

    }
}
