package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.UserDto;

public class UserMapper {
    private UserMapper() {
        // prevent instantiation
    }

    // ================================
    // Entity → DTO
    // ================================

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .storeId(user.getStore() != null ? user.getStore().getId() : null)
                .branchId(user.getBranch() != null ? user.getBranch().getId() : null)
                .updatedAt(user.getUpdatedAt())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();

    }

    // ================================
    // DTO → Entity (NO password here)
    // ================================

    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .role(dto.getRole())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .lastLogin(dto.getLastLogin())
                .build();
    }

    // ================================
    // Update Existing Entity
    // ================================

    public static void updateEntity(User user, UserDto dto) {
        if (user == null || dto == null) {
            return;
        }

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setLastLogin(dto.getLastLogin());
        user.setUpdatedAt(dto.getUpdatedAt());
    }

}
