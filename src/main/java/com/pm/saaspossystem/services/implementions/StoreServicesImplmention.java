package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.StoreMapper;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.UserRoleMapping;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.repository.UserRoleMappingRepository;
import com.pm.saaspossystem.services.StoreServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServicesImplmention implements StoreServices {

    private final StoreRepository storeRepository;
    private final UserService userService;
    private final UserRoleMappingRepository userRoleMappingRepository;

    @Override
    public StoreDto createStore(StoreDto storeDto, UserDto user) throws UserExceptions {
//1. check its admin or NOT
        user.getRoles().stream()
                .filter(role -> role == RoleName.ADMIN)
                .findFirst()
                .orElseThrow(() -> new UserExceptions("User doesn't have permission to create store"));


        // 2. Prevent duplicate store code
        if (storeRepository.findByStoreCode(storeDto.getStoreCode()).isPresent()) {
            throw new IllegalArgumentException(STR."Store code already exists: \{storeDto.getStoreCode()}");
        }

        Store store = StoreMapper.toEntity(storeDto);
        store.setCreatedBy(UserMapper.toEntity(user));

        log.info("Creating store with code: {}", store);
        Store savedStore = storeRepository.save(store);
        log.info("Store created: {}", savedStore.getStoreCode());

        return StoreMapper.toDto(savedStore);
    }

    @Override
    public StoreDto assigneStoreManager(Long storeId,Long managerId, UserDto user) throws UserExceptions {

//        1. its should be admin
        // 2. store should be created by this logined user
        //3. check store exist or not

        // 4. managerID or user should be exist or not

        // 4a. manager should have store Manager as Role table and also if present userRoleMappping. present or not

        // 4b. list. of RoleId and userId/managerId present ...check store is mapped or not
                // 4bA. storeID != managerId
        // not mapped -- mapped that
        // update store as storeManageris Assibnged
        //


        // 1. Caller must be ADMIN
        user.getRoles().stream()
                .filter(role -> role == RoleName.ADMIN)
                .findFirst()
                .orElseThrow(() -> new UserExceptions("User doesn't have permission to assign store manager"));

        // 2. Fetch admin from DB
//        UserDto admin = userService.getUserByEmail(user.getEmail());

        // 3. Store must exist
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new UserExceptions("Store not found with id: " + storeId));

        // 4. Store must belong to this admin
        if (!store.getCreatedBy().getId().equals(user.getId())) {
            throw new UserExceptions("You are not authorized to manage this store");
        }

        // 5. Manager (user) must exist
        UserDto manager = userService.getUserById(managerId);

        // 6. Manager must have STORE_MANAGER role in Role table
        boolean hasStoreManagerRole = manager.getRoles()
                .contains(RoleName.STORE_MANAGER);

        if (!hasStoreManagerRole) {
            throw new UserExceptions("User id: " + managerId + " does not have STORE_MANAGER role");
        }

        // 7. Check UserRoleMapping — is this manager already assigned to a store?
        boolean alreadyMapped = userRoleMappingRepository
                .existsByUserIdAndRoleName(managerId, RoleName.STORE_MANAGER);

        if (alreadyMapped) {
            // 7a. Is it mapped to a DIFFERENT store?
            UserRoleMapping existingMapping = userRoleMappingRepository
                    .findByUserIdAndRoleName(managerId, RoleName.STORE_MANAGER);

            if(existingMapping.getStore().getId() != null){
                throw new UserExceptions(
                        STR."Manager is already assigned to a different store: \{existingMapping.getStore().getId()}"
                );
            }

            log.info(STR."manager ID and SM mapping existing \{existingMapping}");
            if (!existingMapping.getStore().getId().equals(storeId)) {
                throw new UserExceptions(
                        STR."Manager is already assigned to a different store: \{existingMapping.getStore().getId()}"
                );
            }

            // 7b. Already mapped to THIS store — idempotent, just return
            log.info("Manager {} is already assigned to store {}", managerId, storeId);
            return StoreMapper.toDto(store);
        }

        // 8. Create UserRoleMapping for this manager → store
        UserRoleMapping mapping = UserRoleMapping.builder()
                .store(store)
                .assignedBy(UserMapper.toEntity(user))
                .assignedAt(java.time.LocalDateTime.now())
                .build();
        log.info("Creating UserRoleMapping for manager {} → store {}"+ mapping);
        userRoleMappingRepository.save(mapping);
        log.info("UserRoleMapping saved for manager {} → store {}", managerId, storeId);

        // 9. Set storeManager on Store entity (OneToOne)
        store.setStoreManager(UserMapper.toEntity(manager));
        Store updatedStore = storeRepository.save(store);
        log.info("Store {} updated with storeManager {}", storeId, managerId);

        return StoreMapper.toDto(updatedStore);

    }


    // asssinge Store manager

    @Override
    public StoreDto getStoreById(Long storeId) throws Exception {
        Store store = storeRepository.findById(storeId).orElseThrow(()->new Exception("Store not yet Established"));

        return StoreMapper.toDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
       List<Store> stores = storeRepository.findAll();
       return stores.stream().map(StoreMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Store getStoreByAdmin() throws UserExceptions {
        UserDto admin =userService.getCurrentUser();
        return storeRepository.findByStoreAdminId(admin.getId());
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) throws Exception {
        UserDto currentUser =userService.getCurrentUser();
        Store existingStore =storeRepository.findByStoreAdminId(currentUser.getId());

        if(existingStore ==null){
            throw  new Exception("Store not available");
        }

        existingStore.setBrand(storeDto.getBrand());
        existingStore.setDescription(storeDto.getDescription());

        if(storeDto.getStoreType() !=null){
            existingStore.setStoreType(storeDto.getStoreType());
        }

//        if(storeDto.getContact()!=null){
////            StoreContactDto contactDto =StoreContactDto.builder()
////                    .address(storeDto.getContact().getAddress())
////                    .phone(storeDto.getContact().getPhone())
////                    .email(storeDto.getContact().getEmail())
////                    .build();
//
////            StoreContactDto contactDto = StoreMapper.toContactDto(storeDto.getContact()) ;
////            //mapper
////            existingStore.setContact(StoreMapper.toContactEntity(contactDto));
//            existingStore.setContact(StoreMapper.toContactEntity(storeDto.getContact()));
//        }

        Store updateStore = storeRepository.save(existingStore);
        return StoreMapper.toDto(updateStore);
    }

    @Override
    public StoreDto deleteStore(Long id) throws UserExceptions {
        Store store = getStoreByAdmin();
        if(!Objects.equals(id, store.getId())){
            throw new IllegalIdentifierException("Illegal Operation");
        }

        store.setStatus(StoreStatus.BLOCKED);

        Store updateStore =storeRepository.save(store);
        //soft that.. just block it
        return StoreMapper.toDto(updateStore);
    }

    @Override
    public void deleteStore() throws UserExceptions {
        Store store = getStoreByAdmin();
        storeRepository.delete(store);

    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus storeStatus) throws UserExceptions {
        Store store = storeRepository.findById(id).orElseThrow(()-> new UserExceptions("Store not found"));

        store.setStatus(storeStatus);
        Store updateStore =storeRepository.save(store);

        return StoreMapper.toDto(updateStore);
    }

    @Override
    public StoreDto getStoreByEmployee() throws UserExceptions {
        UserDto currentUser = userService.getCurrentUser();

        if(currentUser ==null){
            throw  new UserExceptions("User havn't permission to access");
        }
//        log.info(STR."current user \{currentUser}");
        Long storeId =currentUser.getStoreId();
        return  StoreMapper.toDto(storeRepository.findById(storeId).orElseThrow(()-> new UserExceptions("Store not found")));
    }
}
