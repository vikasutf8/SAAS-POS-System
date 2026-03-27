package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.model.UserRoleMapping;
import com.pm.saaspossystem.payload.dto.RoleMappingDto;
import com.pm.saaspossystem.payload.dto.UserDto;

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
                .id(user.getId())
                .fullName(user.getFullName())
                // password intentionally omitted
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.isActive())
                .storeId(user.getStore() != null ? user.getStore().getId() : null)
                .roleMappings(toRoleMappingDtoList(user.getRoleMappings()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }

    private static List<RoleMappingDto> toRoleMappingDtoList(Set<UserRoleMapping> mappings) {
        if (mappings == null || mappings.isEmpty()) return Collections.emptyList();

        return mappings.stream()
                .map(UserMapper::toRoleMappingDto)
                .collect(Collectors.toList());
    }

    private static RoleMappingDto toRoleMappingDto(UserRoleMapping mapping) {
        return RoleMappingDto.builder()
                .id(mapping.getId())
                .roleName(mapping.getRole().getName())
                .storeId(mapping.getStore() != null ? mapping.getStore().getId() : null)
                .branchId(mapping.getBranch() != null ? mapping.getBranch().getId() : null)
                .assignedById(mapping.getAssignedBy().getId())
                .assignedByName(mapping.getAssignedBy().getFullName())
                .assignedAt(mapping.getAssignedAt())
                .build();
    }

    // ─────────────────────────────────────────
    // DTO → Entity  (creation only — no roleMappings here)
    // ─────────────────────────────────────────

    /**
     * Builds a bare User entity from DTO.
     * Role assignments are NOT set here — they are constructed and
     * persisted separately via UserRoleMapping in the service layer,
     * because each mapping needs a resolved Role and optionally
     * a Store/Branch entity reference.
     */
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        return User.builder()
                .fullName(dto.getFullName())
                .password(dto.getPassword())   // raw here — hash in service before saving
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .isActive(true)
                // store resolved via service using dto.getStoreId()
                .build();
    }

    // ─────────────────────────────────────────
    // Update Existing Entity (PATCH-style)
    // ─────────────────────────────────────────

    /**
     * Updates only the mutable profile fields.
     * Role/store/branch changes go through dedicated service methods
     * that manipulate UserRoleMapping rows directly.
     */
    public static void updateEntity(User user, UserDto dto) {
        if (user == null || dto == null) return;

        if (dto.getFullName() != null)  user.setFullName(dto.getFullName());
        if (dto.getPhone() != null)     user.setPhone(dto.getPhone());
        // email/password changes should go through their own verified flows,
        // not a generic update — intentionally excluded here
    }
}