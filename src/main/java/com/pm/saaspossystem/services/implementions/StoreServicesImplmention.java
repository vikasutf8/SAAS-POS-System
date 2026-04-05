package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.StoreMapper;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.model.UserRoleMapping;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.repository.UserRoleMappingRepository;
import com.pm.saaspossystem.services.StoreServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServicesImplmention implements StoreServices {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserRoleMappingRepository userRoleMappingRepository;

    @Override
    public StoreDto createStore(StoreDto storeDto, UserDto user) throws UserExceptions {

        // get Id of user
        Long userId =user.getId();
        User loginUser =userRepository.findById(userId).orElseThrow(()-> new UserExceptions("User not found with id: " + userId));
        //
        loginUser.getUserRoleMappings().stream()
                .filter(mapping -> mapping.getRole().getName() == RoleName.ADMIN)
                .findFirst()
                .orElseThrow(() -> new UserExceptions("User doesn't have permission to create store"));


        // 2. Prevent duplicate store code
        if (storeRepository.findByStoreCode(storeDto.getStoreCode()).isPresent()) {
            throw new IllegalArgumentException(STR."Store code already exists: \{storeDto.getStoreCode()}");
        }

        Store store = StoreMapper.toEntity(storeDto);
        store.setCreatedBy(loginUser);

        log.info("Creating store with code: {}", store);
        Store savedStore = storeRepository.save(store);
        log.info("Store created: {}", savedStore.getStoreCode());

        return StoreMapper.toDto(savedStore);
    }

    @Override
    public StoreDto assigneStoreManager(Long storeId,Long managerId, UserDto user) throws UserExceptions {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new UserExceptions(STR."Store not found with id: \{storeId}"));

        User storeManager = userRepository.findById(managerId)
                .orElseThrow(() -> new UserExceptions(STR."User not found with id: \{managerId}"));
        Set<UserRoleMapping> storeManagerUserRoleMapping  = storeManager.getUserRoleMappings();
        log.info(STR."Store manager user role mapping \{storeManagerUserRoleMapping}");
        User loginUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserExceptions(STR."User not found with id: \{user.getId()}"));
        Set<UserRoleMapping> loginUserRoleMapping  =loginUser.getUserRoleMappings();
        log.info(STR."login user role mapping \{loginUserRoleMapping}");



        // mangerID == UserID Error
        if(managerId.equals(user.getId())){
            throw new UserExceptions("You cannot assign yourself as store manager");
        }
        // user should be admin
        loginUserRoleMapping.stream()
                .filter(mapping -> mapping.getRole().getName() != RoleName.ADMIN)
                .findFirst()
                .orElseThrow(() -> new UserExceptions("Admin user only assign store manager"));

        // store createById should be user of it
        if(!store.getCreatedBy().getId().equals(user.getId())){
            throw new UserExceptions("You are not authorized to manage this store");
        }

        // manager not be admin
        storeManagerUserRoleMapping.stream()
                .filter(mapping -> mapping.getRole().getName() == RoleName.ADMIN)
                .findFirst()
                .ifPresent(mapping -> {
                    try {
                        throw new UserExceptions(STR."User with id: \{managerId} cannot be assigned as store manager because they have ADMIN role");
                    } catch (UserExceptions e) {
                        throw new RuntimeException(e);
                    }
                });

        // it should be STORE_MANAGER
        storeManagerUserRoleMapping.stream()
                .filter(mapping -> mapping.getRole().getName() == RoleName.STORE_MANAGER)
                .findFirst()
                .orElseThrow(() -> new UserExceptions(STR."User with id: \{managerId} cannot be assigned as store manager because they don't have STORE_MANAGER role"));

        // in manage row have to storeid and getting its id of userRoleMapping in which we have to store
// TODO:


        store.setStoreManager(storeManager);
        Store updatedStore = storeRepository.save(store);


        // 8. Create UserRoleMapping for this manager → store ---dont create
        UserRoleMapping mapping = UserRoleMapping.builder()
                .store(store)
                .assignedBy(UserMapper.toEntity(user))
                .assignedAt(java.time.LocalDateTime.now())
                .build();

        userRoleMappingRepository.save(mapping);


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
