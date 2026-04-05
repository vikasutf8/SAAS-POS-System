package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.BranchMapper;
import com.pm.saaspossystem.model.*;
import com.pm.saaspossystem.payload.dto.BranchDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.*;
import com.pm.saaspossystem.services.BranchService;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImplmentions implements BranchService {
    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final RoleRepository roleRepository;

    @Override
    public BranchDto createBranch(BranchDto branchDto,UserDto userDto) throws UserExceptions {

        User loginUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserExceptions("User not found: " + userDto.getEmail()));

        Set<UserRoleMapping> loginUserRoles = loginUser.getUserRoleMappings();
        Long assoicateStoreId =loginUser.getStore().getId();

        // find loginUser is ADMIN or STORE_MANAGER
        boolean isAdmin = loginUserRoles.stream()
                .anyMatch(mapping -> mapping.getRole().getName() == RoleName.ADMIN);
        boolean isStoreManager = loginUserRoles.stream()
                .anyMatch(mapping -> mapping.getRole().getName() == RoleName.STORE_MANAGER);


        // in this set find having storeID and userID
        UserRoleMapping userRoleMapping =userRoleMappingRepository.findByUserIdAndStoreId(loginUser.getId(),assoicateStoreId);

        if (!isAdmin && !isStoreManager) {
            throw new UserExceptions("Only ADMIN or STORE_MANAGER can create branch");
        }
         branchDto.setStoreId(assoicateStoreId);
        branchDto.setCreatedById(loginUser.getId());

        Branch savedBranch = branchRepository.save(BranchMapper.toEntity(branchDto));


        userRoleMapping.setBranch(savedBranch);

        userRoleMappingRepository.save(userRoleMapping);

        return BranchMapper.toDto(savedBranch);
    }
    /*
    **Updated rules clearly:**
```
STORE_MANAGER calling
    ├── managerId == caller (self)
    │       → allowed
    │       → SM max 1 branch as BM — throw if already BM somewhere
    │
    └── managerId != caller (someone else)
            → allowed
            → target must have BRANCH_MANAGER role
            → target max 2 branches — throw if already at limit

ADMIN calling
    └── managerId != caller (someone else)
            → target must have BRANCH_MANAGER role
            → target max 2 branches — throw if already at limit
     */

    @Override
    public BranchDto assigneBranchManager(Long branchId, Long managerId, UserDto userDto) throws UserExceptions {

        // 1. Caller must be ADMIN or STORE_MANAGER
        boolean isAdmin = userDto.getRoles().contains(RoleName.ADMIN);
        boolean isStoreManager = userDto.getRoles().contains(RoleName.STORE_MANAGER);

        if (!isAdmin && !isStoreManager) {
            throw new UserExceptions("Only ADMIN or STORE_MANAGER can assign branch manager");
        }

        // 2. Fetch caller from DB (real entity — need id for assignedBy FK)
        User caller = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserExceptions("Caller not found"));

        // 3. Fetch branch from DB
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new UserExceptions("Branch not found with id: " + branchId));

        Store store = branch.getStore();

        // 4. Ownership check
        //    ADMIN       → must have created the store
        //    SM          → must be assigned manager of this branch's store
        if (isAdmin && !store.getCreatedBy().getId().equals(caller.getId())) {
            throw new UserExceptions("Admin is not the owner of this store");
        }

        if (isStoreManager && (store.getStoreManager() == null
                || !store.getStoreManager().getId().equals(caller.getId()))) {
            throw new UserExceptions("Store Manager is not assigned to this store");
        }

        // 5. Two cases based on who is being assigned
        User manager;

        if (isStoreManager ) {
            // ── CASE A: SM assigning — themselves OR someone else ──

            if (caller.getId().equals(managerId)) {
                // SM assigning themselves
                manager = caller;

                // SM can only be BM of max 1 branch
                long smAsBMCount = userRoleMappingRepository
                        .countByUserIdAndRoleName(managerId, RoleName.BRANCH_MANAGER);

                if (smAsBMCount >= 1) {
                    throw new UserExceptions("Store Manager is already a Branch Manager of another branch");
                }

            } else {
                // SM assigning someone else
                manager = userRepository.findById(managerId)
                        .orElseThrow(() -> new UserExceptions("Manager not found with id: " + managerId));

                if (!manager.isActive()) {
                    throw new UserExceptions("Manager account is not active");
                }

//                // That person must have BRANCH_MANAGER role
//                if (!manager.getRoleNames().contains(RoleName.BRANCH_MANAGER)) {
//                    throw new UserExceptions("User " + managerId + " does not have BRANCH_MANAGER role");
//                }

                // BRANCH_MANAGER can manage max 2 branches
                long existingBranchCount = userRoleMappingRepository
                        .countByUserIdAndRoleName(managerId, RoleName.BRANCH_MANAGER);

                if (existingBranchCount >= 2) {
                    throw new UserExceptions("Branch Manager is already managing 2 branches (max limit)");
                }
            }

        } else if (isAdmin && !caller.getId().equals(managerId)) {
            // ── CASE B: ADMIN assigning someone else as BM ──
            manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new UserExceptions("Manager not found with id: " + managerId));

            if (!manager.isActive()) {
                throw new UserExceptions("Manager account is not active");
            }

            // Manager must have BRANCH_MANAGER role
//            if (!manager.getRoleNames().contains(RoleName.BRANCH_MANAGER)) {
//                throw new UserExceptions(STR."User \{managerId} does not have BRANCH_MANAGER role");
//            }

            // BRANCH_MANAGER can manage max 2 branches
            long existingBranchCount = userRoleMappingRepository
                    .countByUserIdAndRoleName(managerId, RoleName.BRANCH_MANAGER);

            if (existingBranchCount >= 2) {
                throw new UserExceptions("Branch Manager is already managing 2 branches (max limit)");
            }

        } else {
            // SM trying to assign someone else → not allowed
            throw new UserExceptions("Store Manager can only assign themselves as branch manager");
        }

        // 6. Check if this branch already has a manager
        if (branch.getBranchManager() != null) {
            throw new UserExceptions("Branch already has a manager assigned");
        }

        // 7. Fetch BRANCH_MANAGER Role entity from DB
        Role branchManagerRole = roleRepository.findByName(RoleName.BRANCH_MANAGER)
                .orElseThrow(() -> new UserExceptions("BRANCH_MANAGER role not found in DB"));

        // 8. Save UserRoleMapping
        UserRoleMapping mapping = UserRoleMapping.builder()
                .user(manager)
                .role(branchManagerRole)
                .store(store)
                .branch(branch)
                .assignedBy(caller)     // real DB entity ✓
                .build();

        userRoleMappingRepository.save(mapping);
        log.info("UserRoleMapping saved: manager {} → branch {}", manager.getId(), branchId);

        // 9. Set branchManager on Branch entity
        branch.setBranchManager(manager);   // real DB entity ✓
        Branch updatedBranch = branchRepository.save(branch);
        log.info("Branch {} updated with branchManager {}", branchId, manager.getId());

        return BranchMapper.toDto(updatedBranch);
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

//        Branch branch = branchRepository.findById(id)
//                .orElseThrow(() -> new UserExceptions("Branch not found"));
//
//
//
//        // 2️⃣ Get current logged-in user
//        UserDto currentUser = userService.getCurrentUser();
//
//        // 3️⃣ Get store of current admin
//        Store store = storeRepository.findByStoreAdminId(currentUser.getId());
//
////        Optional<Branch> existingBranch =
////                branchRepository.findByNameAndStoreId(branchDto.getName(), store.getId());
////
////        if (existingBranch.isPresent() &&
////                !existingBranch.get().getId().equals(branchId)) {
////
////            throw new RuntimeException("Branch with same name already exists in this store");
////        }
//
////        if (store == null) {
////            throw new UserExceptions("Store not found for current admin");
////        }
//
//        // 4️⃣ Load manager from DB (NEVER trust DTO directly)
//        User manager = UserMapper.toEntity(currentUser);
////
////        if (branchDto.getManager() != null &&
////                branchDto.getManager().getId() != null) {
////
////            manager = userRepository.findById(branchDto.getManager().getId())
////                    .orElseThrow(() -> new UserExceptions("Manager not found"));
////        }
//
//        // 5️⃣ Update existing entity
//        BranchMapper.updateEntity(branch, branchDto, store, manager);
//
//        // 6️⃣ Save
//        Branch updatedBranch = branchRepository.save(branch);
//
//        return BranchMapper.toDto(updatedBranch);
        return null;
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
