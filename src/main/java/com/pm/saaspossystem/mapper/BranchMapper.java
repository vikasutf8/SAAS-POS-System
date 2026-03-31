package com.pm.saaspossystem.mapper;

//public class BranchMapper {
//
//    private BranchMapper() {
//        // prevent instantiation
//    }
//
//    // =========================================
//    // Entity → DTO
//    // =========================================
//    public static BranchDto toDto(Branch branch) {
//
//        if (branch == null) {
//            return null;
//        }
//
//        return BranchDto.builder()
//                .id(branch.getId())
//                .name(branch.getName())
//                .address(branch.getAddress())
//                .phone(branch.getPhone())
//                .email(branch.getEmail())
//                .workingDays(branch.getWorkingDays())
//                .openTime(branch.getOpenTime())
//                .closeTime(branch.getCloseTime())
//                .storeId(
//                        branch.getStore() != null
//                                ? branch.getStore().getId()
//                                : null
//                )
////                .managerId(
////                        branch.getManager() != null
////                                ? branch.getManager().getId()
////                                : null
////                )
//                .createdAt(branch.getCreatedAt())
//                .updatedAt(branch.getUpdatedAt())
//                .build();
//    }
//
//    // =========================================
//    // DTO → Entity (For Create)
//    // =========================================
//    public static Branch toEntity(BranchDto dto,
//                                  Store store,
//                                  User manager) {
//
//        if (dto == null) {
//            return null;
//        }
//
//        return Branch.builder()
//                .id(dto.getId())
//                .name(dto.getName())
//                .address(dto.getAddress())
//                .phone(dto.getPhone())
//                .email(dto.getEmail())
//                .workingDays(dto.getWorkingDays())
//                .openTime(dto.getOpenTime())
//                .closeTime(dto.getCloseTime())
//                .store(store)
////                .manager(manager)
//                .build();
//    }
//
//    // =========================================
//    // Update Existing Entity
//    // =========================================
//    public static void updateEntity(Branch branch,
//                                    BranchDto dto,
//                                    Store store,
//                                    User manager) {
//
//        if (branch == null || dto == null) {
//            return;
//        }
//
//        branch.setName(dto.getName());
//        branch.setAddress(dto.getAddress());
//        branch.setPhone(dto.getPhone());
//        branch.setEmail(dto.getEmail());
//        branch.setWorkingDays(dto.getWorkingDays());
//        branch.setOpenTime(dto.getOpenTime());
//        branch.setCloseTime(dto.getCloseTime());
//
//        if (store != null) {
//            branch.setStore(store);
//        }
//
//        if (manager != null) {

import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.payload.dto.BranchDto;

import java.util.ArrayList;

////            branch.setManager(manager);
//        }
//    }
//}


public class BranchMapper {

    private BranchMapper() {}

    // ─────────────────────────────────────────
    // Entity → DTO
    // ─────────────────────────────────────────

    public static BranchDto toDto(Branch branch) {
        if (branch == null) return null;

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
                        branch.getStore() != null ? branch.getStore().getId() : null
                )
                .createdById(
                        branch.getCreatedBy() != null ? branch.getCreatedBy().getId() : null
                )
                .branchManagerId(
                        branch.getBranchManager() != null ? branch.getBranchManager().getId() : null
                )
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    // ─────────────────────────────────────────
    // DTO → Entity  (creation only)
    // ─────────────────────────────────────────

    /**
     * Builds a bare Branch entity.
     * store, createdBy, branchManager are NOT set here —
     * resolved in service layer from repo lookups.
     */
    public static Branch toEntity(BranchDto dto) {
        if (dto == null) return null;

        return Branch.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .workingDays(
                        dto.getWorkingDays() != null ? dto.getWorkingDays() : new ArrayList<>()
                )
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                // store     → set in service
                // createdBy → set in service
                // branchManager → set in service if branchManagerId present
                .build();
    }

    // ─────────────────────────────────────────
    // Update Existing Entity (PATCH-style)
    // ─────────────────────────────────────────

    public static void updateEntity(Branch branch, BranchDto dto) {
        if (branch == null || dto == null) return;

        if (dto.getName() != null)        branch.setName(dto.getName());
        if (dto.getAddress() != null)     branch.setAddress(dto.getAddress());
        if (dto.getPhone() != null)       branch.setPhone(dto.getPhone());
        if (dto.getEmail() != null)       branch.setEmail(dto.getEmail());
        if (dto.getWorkingDays() != null) branch.setWorkingDays(dto.getWorkingDays());
        if (dto.getOpenTime() != null)    branch.setOpenTime(dto.getOpenTime());
        if (dto.getCloseTime() != null)   branch.setCloseTime(dto.getCloseTime());
    }
}
