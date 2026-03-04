package com.pm.saaspossystem.services;

import com.pm.saaspossystem.payload.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    InventoryDto createInventory(InventoryDto inventoryDto);
    InventoryDto updateInventory(Long id, InventoryDto inventoryDto);
    void deleteInventory(Long id);
    InventoryDto getInventoryById(Long id);
    InventoryDto getInventoryByProductIdAndBranchId(Long productId, Long branchId);
    List<InventoryDto> getAllInventoryByBranchId(Long branchId);


}
