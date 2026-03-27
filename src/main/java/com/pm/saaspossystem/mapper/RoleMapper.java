package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Role;
import com.pm.saaspossystem.payload.dto.RoleDto;

public class RoleMapper {

    private RoleMapper() {}

    public static RoleDto toDto(Role  role) {
        if (role == null) return null;

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Role toEntity(RoleDto dto) {
        if (dto == null) return null;

        return Role.builder()
                .name(dto.getName())
                .build();
    }
}