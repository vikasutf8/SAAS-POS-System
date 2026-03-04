package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.payload.dto.InventoryDto;
import com.pm.saaspossystem.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // =========================================
    // CREATE INVENTORY
    // =========================================
    @PostMapping
    public ResponseEntity<InventoryDto> createInventory(
            @RequestBody InventoryDto inventoryDto) {

        InventoryDto created =
                inventoryService.createInventory(inventoryDto);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // =========================================
    // UPDATE INVENTORY
    // =========================================
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable Long id,
            @RequestBody InventoryDto inventoryDto) {

        InventoryDto updated =
                inventoryService.updateInventory(id, inventoryDto);

        return ResponseEntity.ok(updated);
    }

    // =========================================
    // DELETE INVENTORY
    // =========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================
    // GET INVENTORY BY ID
    // =========================================
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(
            @PathVariable Long id) {

        InventoryDto inventory =
                inventoryService.getInventoryById(id);

        return ResponseEntity.ok(inventory);
    }

    // =========================================
    // GET INVENTORY BY PRODUCT + BRANCH
    // =========================================
    @GetMapping("/search")
    public ResponseEntity<InventoryDto> getByProductAndBranch(
            @RequestParam Long productId,
            @RequestParam Long branchId) {

        InventoryDto inventory =
                inventoryService
                        .getInventoryByProductIdAndBranchId(productId, branchId);

        return ResponseEntity.ok(inventory);
    }

    // =========================================
    // GET ALL INVENTORY BY BRANCH
    // =========================================
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDto>> getAllByBranch(
            @PathVariable Long branchId) {

        List<InventoryDto> inventoryList =
                inventoryService.getAllInventoryByBranchId(branchId);

        return ResponseEntity.ok(inventoryList);
    }
}
