package com.pm.saaspossystem.services;

import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;

import java.util.List;

public interface StoreServices
{

    StoreDto createStore(StoreDto storeDto, UserDto user);
    StoreDto getStoreById(Long storeId) throws Exception;
    List<StoreDto> getAllStores();
    Store getStoreByAdmin() throws UserExceptions;
    StoreDto updateStore(Long id, StoreDto storeDto) throws Exception;
    StoreDto deleteStore(Long id) throws UserExceptions; //soft deleted
    StoreDto getStoreByEmployee() throws UserExceptions;
    void deleteStore() throws UserExceptions;
    StoreDto moderateStore(Long id, StoreStatus storeStatus) throws UserExceptions;
}
