package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.BranchMapper;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
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
        log.info("Store found for current user: {}", store);
        Branch branch = BranchMapper.toEntity(branchDto, store, UserMapper.toEntity(currentUser));

        Branch savedBranch = branchRepository.save(branch);


        // set barnch in that
        currentUser.setBranchId(savedBranch.getId());
        userRepository.save(UserMapper.toEntity(currentUser));

        return BranchMapper.toDto(savedBranch);
    }

    @Override
    public BranchDto updateBranch(Long id, BranchDto branchDto) throws UserExceptions {

//        Branch branch = branchRepository.findById(id).orElseThrow(()->new UserExceptions("Branch not found"))
//
//        UserDto currentUser = userService.getCurrentUser();
//        Store store = storeRepository.findByStoreAdminId(currentUser.getId());
//
//        Branch updatedBranch = branchRepository.save(BranchMapper.updateEntity(branch,branchDto,store,UserMapper.toEntity(currentUser)));
//
//        return BranchMapper.toDto(updatedBranch);

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new UserExceptions("Branch not found"));



        // 2️⃣ Get current logged-in user
        UserDto currentUser = userService.getCurrentUser();

        // 3️⃣ Get store of current admin
        Store store = storeRepository.findByStoreAdminId(currentUser.getId());

//        Optional<Branch> existingBranch =
//                branchRepository.findByNameAndStoreId(branchDto.getName(), store.getId());
//
//        if (existingBranch.isPresent() &&
//                !existingBranch.get().getId().equals(branchId)) {
//
//            throw new RuntimeException("Branch with same name already exists in this store");
//        }

//        if (store == null) {
//            throw new UserExceptions("Store not found for current admin");
//        }

        // 4️⃣ Load manager from DB (NEVER trust DTO directly)
        User manager = UserMapper.toEntity(currentUser);
//
//        if (branchDto.getManager() != null &&
//                branchDto.getManager().getId() != null) {
//
//            manager = userRepository.findById(branchDto.getManager().getId())
//                    .orElseThrow(() -> new UserExceptions("Manager not found"));
//        }

        // 5️⃣ Update existing entity
        BranchMapper.updateEntity(branch, branchDto, store, manager);

        // 6️⃣ Save
        Branch updatedBranch = branchRepository.save(branch);

        return BranchMapper.toDto(updatedBranch);
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
