package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.StoreServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return storeServices.createStore(storeDto, UserMapper.toEntity(currentUser));
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

    // =========================================
    // Soft Delete Store By ID
    // =========================================
    @PutMapping("/{id}")
    public StoreDto deleteStore(@PathVariable Long id) throws UserExceptions {
        return storeServices.deleteStore(id);
    }

    // =========================================
    // Delete Store Of Current Admin
    // =========================================
    @DeleteMapping
    public void deleteCurrentUserStore() throws UserExceptions {
        storeServices.deleteStore();
    }
}
