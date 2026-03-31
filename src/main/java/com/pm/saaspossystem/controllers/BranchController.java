package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.BranchDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.BranchService;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/branch")
public class BranchController {
    private final BranchService branchService;
    private  final UserService userService;

    // =========================================
    // CREATE BRANCH
    // =========================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
    public ResponseEntity<BranchDto> createBranch(
            @RequestBody BranchDto branchDto) throws UserExceptions {
        UserDto currentUser = userService.getCurrentUser();
        BranchDto createdBranch = branchService.createBranch(branchDto, currentUser);

        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    // =========================================
    // Assigne Branch manager to Branch
    // =========================================
    @PatchMapping("{branchId}/manager/{managerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
    public BranchDto  assigneStoreManager(
            @RequestParam Long branchId,
            @RequestParam Long managerId
    ) throws UserExceptions {
//        log.info(   "Creating store with data: " + storeDto);
        UserDto currentUser = userService.getCurrentUser();
        log.info(STR."user crrent \{currentUser}");
        return branchService.assigneBranchManager(branchId,managerId,currentUser);
    }

    // =========================================
    // UPDATE BRANCH
    // =========================================
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<BranchDto> updateBranch(
            @PathVariable Long id,
            @RequestBody BranchDto branchDto) throws UserExceptions {

        BranchDto updatedBranch = branchService.updateBranch(id, branchDto);

        return ResponseEntity.ok(updatedBranch);
    }

    // =========================================
    // DELETE BRANCH
    // =========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {

        branchService.deleteBranch(id);

        return ResponseEntity.ok("Branch deleted successfully");
    }

    // =========================================
    // GET ALL BRANCHES BY STORE
    // =========================================
    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<BranchDto>> getAllBranchesByStore(
            @PathVariable Long storeId) {

        List<BranchDto> branches =
                branchService.getAllBranchByStoreId(storeId);

        return ResponseEntity.ok(branches);
    }

    // =========================================
    // GET BRANCH BY ID
    // =========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<BranchDto> getBranchById(
            @PathVariable Long id) {

        BranchDto branch = branchService.getBranchById(id);

        return ResponseEntity.ok(branch);
    }
}
