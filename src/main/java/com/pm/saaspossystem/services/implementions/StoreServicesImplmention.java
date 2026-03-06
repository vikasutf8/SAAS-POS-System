package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.StoreMapper;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.StoreServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StoreServicesImplmention implements StoreServices {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = StoreMapper.toEntity(storeDto,user);

        return StoreMapper.toDto(storeRepository.save(store));
    }

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
        existingStore.setBranch(storeDto.getBranch());
        existingStore.setDescription(storeDto.getDescription());

        if(storeDto.getStoreType() !=null){
            existingStore.setStoreType(storeDto.getStoreType());
        }

        if(storeDto.getContact()!=null){
//            StoreContactDto contactDto =StoreContactDto.builder()
//                    .address(storeDto.getContact().getAddress())
//                    .phone(storeDto.getContact().getPhone())
//                    .email(storeDto.getContact().getEmail())
//                    .build();

//            StoreContactDto contactDto = StoreMapper.toContactDto(storeDto.getContact()) ;
//            //mapper
//            existingStore.setContact(StoreMapper.toContactEntity(contactDto));
            existingStore.setContact(StoreMapper.toContactEntity(storeDto.getContact()));
        }

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
            throw  new UserExceptions("User havn;t permission to access");
        }
        return  StoreMapper.toDto(currentUser.getStore());
    }
}
