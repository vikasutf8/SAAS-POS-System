package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.StoreServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v2/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreServices storeServices;
    private final UserService userService;

    // =========================================
    // Create Store
    // =========================================
    @PostMapping
    public StoreDto createStore(@RequestBody StoreDto storeDto) throws UserExceptions {

        UserDto currentUser = userService.getCurrentUser();
        log.info("user crrent "+currentUser);
        return storeServices.createStore(storeDto, currentUser);
    }

    // =========================================
    // Get Store By ID
    // =========================================
    @GetMapping("/{id}")
    public StoreDto getStoreById(@PathVariable Long id) throws Exception {
        return storeServices.getStoreById(id);
    }

    // =========================================
    // Get All Stores
    // =========================================
    @GetMapping
    public List<StoreDto> getAllStores() {
        return storeServices.getAllStores();
    }

    // =========================================
    // Get Store By Admin (Current User)
    // =========================================
    @GetMapping("/admin")
    public Store getStoreByAdmin() throws UserExceptions {
        return storeServices.getStoreByAdmin();
    }

    // =========================================
    // Get Store By Employee
    // =========================================
    @GetMapping("/employee")
    public StoreDto getStoreByEmployee() throws UserExceptions {
        return storeServices.getStoreByEmployee();
    }

    // =========================================
    // Update Store
    // =========================================
    @PutMapping("/{id}")
    public StoreDto updateStore(
            @PathVariable Long id,
            @RequestBody StoreDto storeDto
    ) throws Exception {

        return storeServices.updateStore(id, storeDto);
    }

    @PutMapping("/{id}/status")
    public StoreDto moderateStore(
            @PathVariable Long id,
            @RequestParam StoreStatus storeStatus
    ) throws Exception {

        return storeServices.moderateStore(id,storeStatus);
    }

    // =========================================
    // Soft Delete Store By ID
    // =========================================
    @PutMapping("delete/{id}")
    public StoreDto deleteStore(@PathVariable Long id) throws UserExceptions {
        return storeServices.deleteStore(id);
    }

    // =========================================
    // Delete Store Of Current Admin
    // =========================================
    @DeleteMapping("delete")
    public void deleteCurrentUserStore() throws UserExceptions {
        storeServices.deleteStore();
    }
}
