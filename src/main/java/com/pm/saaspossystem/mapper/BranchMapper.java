package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.BranchDto;

public class BranchMapper {

    private BranchMapper() {
        // prevent instantiation
    }

    // =========================================
    // Entity → DTO
    // =========================================
    public static BranchDto toDto(Branch branch) {

        if (branch == null) {
            return null;
        }

        return BranchDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .email(branch.getEmail())
                .workingDays(branch.getWorkingDays())
                .openTime(branch.getOpenTime())
                .closeTime(branch.getCloseTime())
                .storeId(
                        branch.getStore() != null
                                ? branch.getStore().getId()
                                : null
                )
                .store(
                        branch.getStore() != null
                                ? StoreMapper.toDto(branch.getStore())
                                : null
                )
                .manager(
                        branch.getManager() != null
                                ? UserMapper.toDto(branch.getManager())
                                : null
                )
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    // =========================================
    // DTO → Entity (For Create)
    // =========================================
    public static Branch toEntity(BranchDto dto,
                                  Store store,
                                  User manager) {

        if (dto == null) {
            return null;
        }

        return Branch.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .workingDays(dto.getWorkingDays())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .store(store)
                .manager(manager)
                .build();
    }

    // =========================================
    // Update Existing Entity
    // =========================================
    public static void updateEntity(Branch branch,
                                    BranchDto dto,
                                    Store store,
                                    User manager) {

        if (branch == null || dto == null) {
            return;
        }

        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setWorkingDays(dto.getWorkingDays());
        branch.setOpenTime(dto.getOpenTime());
        branch.setCloseTime(dto.getCloseTime());

        if (store != null) {
            branch.setStore(store);
        }

        if (manager != null) {
            branch.setManager(manager);
        }
    }
}
