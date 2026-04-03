package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Inventory;
import com.pm.saaspossystem.model.Product;
import com.pm.saaspossystem.payload.dto.InventoryDto;

public class InventoryMapper {
    private InventoryMapper() {
        // prevent instantiation
    }

    // =========================================
    // ENTITY → DTO
    // =========================================
    public static InventoryDto toDto(Inventory inventory) {

        if (inventory == null) {
            return null;
        }

        return InventoryDto.builder()
                .id(inventory.getId())
                .branchId(
                        inventory.getBranch() != null
                                ? inventory.getBranch().getId()
                                : null
                )
                .productId(
                        inventory.getProduct() != null
                                ? inventory.getProduct().getId()
                                : null
                )

                .quantity(inventory.getQuantity())
                .build();
    }

    // =========================================
    // DTO → ENTITY (CREATE)
    // =========================================
    public static Inventory toEntity(InventoryDto dto,
                                     Branch branch,
                                     Product product) {

        if (dto == null) {
            return null;
        }

        return Inventory.builder()
                .id(dto.getId())
                .branch(branch)
                .product(product)
                .quantity(dto.getQuantity())
                .build();
    }

    // =========================================
    // UPDATE EXISTING ENTITY
    // =========================================
    public static void updateEntity(Inventory inventory,
                                    InventoryDto dto,
                                    Branch branch,
                                    Product product) {

        if (inventory == null || dto == null) {
            return;
        }

        if (branch != null) {
            inventory.setBranch(branch);
        }

        if (product != null) {
            inventory.setProduct(product);
        }

        if (dto.getQuantity() != null) {
            inventory.setQuantity(dto.getQuantity());
        }
    }
}
