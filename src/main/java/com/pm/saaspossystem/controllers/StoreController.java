package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.StoreDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.StoreServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public StoreDto createStore(@RequestBody StoreDto storeDto) throws UserExceptions {

        UserDto currentUser = userService.getCurrentUser();
        return storeServices.createStore(storeDto, currentUser);
    }

    // =========================================
    // Get Store By ID
    // =========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
    public StoreDto getStoreById(@PathVariable Long id) throws Exception {
        return storeServices.getStoreById(id);
    }

    // =========================================
    // Get All Stores
    // =========================================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<StoreDto> getAllStores() {
        return storeServices.getAllStores();
    }

    // =========================================
    // Get Store By Admin (Current User)
    // =========================================
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Store getStoreByAdmin() throws UserExceptions {
        return storeServices.getStoreByAdmin();
    }

    // =========================================
    // Get Store By Employee
    // =========================================
    @GetMapping("/employee")
    @PreAuthorize("hasRole('STORE_MANAGER') or hasRole('BRANCH_MANAGER') or hasRole('BRANCH_CASHIER')")
    public StoreDto getStoreByEmployee() throws UserExceptions {
        return storeServices.getStoreByEmployee();
    }

    // =========================================
    // Update Store
    // =========================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
    public StoreDto updateStore(
            @PathVariable Long id,
            @RequestBody StoreDto storeDto
    ) throws Exception {

        return storeServices.updateStore(id, storeDto);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
    public StoreDto deleteStore(@PathVariable Long id) throws UserExceptions {
        return storeServices.deleteStore(id);
    }

    // =========================================
    // Delete Store Of Current Admin
    // =========================================
    @DeleteMapping("delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCurrentUserStore() throws UserExceptions {
        storeServices.deleteStore();
    }
}
