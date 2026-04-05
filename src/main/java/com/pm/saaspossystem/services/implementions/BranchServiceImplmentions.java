package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.BranchMapper;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.BranchDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.BranchRepository;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.BranchService;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImplmentions implements BranchService {
    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public BranchDto createBranch(BranchDto branchDto) throws UserExceptions {
        UserDto currentUser = userService.getCurrentUser();
        log.info("Current user: {}", currentUser);
        Store store = storeRepository.findByStoreAdminId(currentUser.getId()); // i haven;t use bydrirectional
//        log.info("Store found for current user: {}", store);
        Branch branch = BranchMapper.toEntity(branchDto, store, UserMapper.toEntity(currentUser));

        Branch savedBranch = branchRepository.save(branch);

        return BranchMapper.toDto(savedBranch);
    }

    @Override
    public BranchDto updateBranch(Long id, BranchDto branchDto) throws UserExceptions {

        Branch existBranch = branchRepository.findById(id).orElseThrow(()->new UserExceptions("Branch not found"));

//        UserDto currentUser = userService.getCurrentUser();
//        Store store = storeRepository.findByStoreAdminId(currentUser.getId());

        existBranch.setName(branchDto.getName());
        existBranch.setAddress(branchDto.getAddress());
        existBranch.setEmail(branchDto.getEmail());
        existBranch.setCloseTime(branchDto.getCloseTime());
        existBranch.setOpenTime(branchDto.getOpenTime());
        existBranch.setUpdatedAt(LocalDateTime.now());
        existBranch.setWorkingDays(branchDto.getWorkingDays());

        Branch updateBranch = branchRepository.save(existBranch);
        return BranchMapper.toDto(updateBranch);

    }

    @Override
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        branchRepository.delete(branch);
    }

    @Override
    public List<BranchDto> getAllBranchByStoreId(Long storeID) {
        // Ensure store exists
        if (!storeRepository.existsById(storeID)) {
            throw new RuntimeException("Store not found");
        }

        return branchRepository.findByStoreId(storeID)
                .stream()
                .map(BranchMapper::toDto)
                .toList();
    }

    @Override
    public BranchDto getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        return BranchMapper.toDto(branch);
    }
}
