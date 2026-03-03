package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.BranchDto;
import com.pm.saaspossystem.services.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/branch")
public class BranchController {
    private final BranchService branchService;

    // =========================================
    // CREATE BRANCH
    // =========================================
    @PostMapping
    public ResponseEntity<BranchDto> createBranch(
            @RequestBody BranchDto branchDto) throws UserExceptions {

        BranchDto createdBranch = branchService.createBranch(branchDto);

        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    // =========================================
    // UPDATE BRANCH
    // =========================================
    @PutMapping("/{id}")
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
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {

        branchService.deleteBranch(id);

        return ResponseEntity.ok("Branch deleted successfully");
    }

    // =========================================
    // GET ALL BRANCHES BY STORE
    // =========================================
    @GetMapping("/store/{storeId}")
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
    public ResponseEntity<BranchDto> getBranchById(
            @PathVariable Long id) {

        BranchDto branch = branchService.getBranchById(id);

        return ResponseEntity.ok(branch);
    }
}
