package com.pm.saaspossystem.mapper;

//public class UserMapper {
//
//    private UserMapper() {}
//
//    // ─────────────────────────────────────────
//    // Entity → DTO
//    // ─────────────────────────────────────────
//
//    public static UserDto toDto(User user) {
//        if (user == null) return null;
//
//        return UserDto.builder()
//                .fullName(user.getFullName())
//                // password intentionally omitted
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .isActive(user.isActive())
//                .storeId(user.getStore() != null ? user.getStore().getId() : null)
//                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
//                .lastLogin(user.getLastLogin())
//                .build();
//    }
//
//    private static List<UserRoleMappingDto> toRoleMappingDtoList(Set<UserRoleMapping> mappings) {
//        if (mappings == null || mappings.isEmpty()) return Collections.emptyList();
//
//        return mappings.stream()
//                .map(UserMapper::toRoleMappingDto)
//                .collect(Collectors.toList());
//    }
//
//    private static UserRoleMappingDto toRoleMappingDto(UserRoleMapping mapping) {
//        return UserRoleMappingDto.builder()
//                .id(mapping.getId())
//                .roleName(mapping.getRole().getName())
//                .storeId(mapping.getStore() != null ? mapping.getStore().getId() : null)
//                .branchId(mapping.getBranch() != null ? mapping.getBranch().getId() : null)
//                .assignedById(mapping.getAssignedBy().getId())
//                .build();
//    }
//
//    // ─────────────────────────────────────────
//    // DTO → Entity  (creation only — no roleMappings here)
//    // ─────────────────────────────────────────
//
//    /**
//     * Builds a bare User entity from DTO.
//     * Role assignments are NOT set here — they are constructed and
//     * persisted separately via UserRoleMapping in the service layer,
//     * because each mapping needs a resolved Role and optionally
//     * a Store/Branch entity reference.
//     */
//    public static User toEntity(UserDto dto) {
//        if (dto == null) return null;
//
//        return User.builder()
//                .fullName(dto.getFullName())
//                .password(dto.getPassword())   // raw here — hash in service before saving
//                .email(dto.getEmail())
//                .phone(dto.getPhone())
//                .isActive(true)
//                // store resolved via service using dto.getStoreId()
//                .build();
//    }
//
//    // ─────────────────────────────────────────
//    // Update Existing Entity (PATCH-style)
//    // ─────────────────────────────────────────
//
//    /**
//     * Updates only the mutable profile fields.
//     * Role/store/branch changes go through dedicated service methods
//     * that manipulate UserRoleMapping rows directly.
//     */
//    public static void updateEntity(User user, UserDto dto) {
//        if (user == null || dto == null) return;
//
//        if (dto.getFullName() != null)  user.setFullName(dto.getFullName());
//        if (dto.getPhone() != null)     user.setPhone(dto.getPhone());
//        // email/password changes should go through their own verified flows,
//        // not a generic update — intentionally excluded here
//    }
//}


import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.model.UserRoleMapping;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.payload.dto.UserRoleMappingDto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    // ─────────────────────────────────────────
    // Entity → DTO
    // ─────────────────────────────────────────

    public static UserDto toDto(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .fullName(user.getFullName())
                // password intentionally omitted
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.isActive())



//                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
//                .lastLogin(user.getLastLogin())
                .build();
    }

    // ─────────────────────────────────────────
    // UserRoleMapping → DTO  (private helpers)
    // ─────────────────────────────────────────

    private static List<UserRoleMappingDto> toRoleMappingDtoList(Set<UserRoleMapping> mappings) {
        if (mappings == null || mappings.isEmpty()) return Collections.emptyList();

        return mappings.stream()
                .map(UserMapper::toRoleMappingDto)
                .collect(Collectors.toList());
    }

    private static UserRoleMappingDto toRoleMappingDto(UserRoleMapping mapping) {
        return UserRoleMappingDto.builder()
                .id(mapping.getId())

                .storeId(mapping.getStore() != null ? mapping.getStore().getId() : null)
                .branchId(mapping.getBranch() != null ? mapping.getBranch().getId() : null)
                .assignedById(mapping.getAssignedBy().getId())
                .build();
    }

    // ─────────────────────────────────────────
    // DTO → Entity  (creation only)
    // ─────────────────────────────────────────

    /**
     * Builds a bare User entity from DTO.
     * Roles and store/branch are NOT set here —
     * service handles those after saving the User.
     */
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        return User.builder()
                .fullName(dto.getFullName())
                .password(dto.getPassword())   // raw — service BCrypts before save
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .isActive(true)
                // store resolved by service using dto.getStoreId()
                .build();
    }

    // ─────────────────────────────────────────
    // Update Existing Entity (PATCH-style)
    // ─────────────────────────────────────────

    /**
     * Updates only mutable profile fields.
     * Role/store/branch changes go through dedicated service methods.
     */
    public static void updateEntity(User user, UserDto dto) {
        if (user == null || dto == null) return;

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getPhone() != null)    user.setPhone(dto.getPhone());
        // email/password → own verified flows, excluded here
    }
}