package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.UserRoleMapping;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.model.Role;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.payload.dto.UserRoleMappingDto;

public class UserRoleMappper {
    // Entity → DTO
    public static UserRoleMappingDto toDto(UserRoleMapping mapping) {
        if (mapping == null) return null;
        return UserRoleMappingDto.builder()
                .id(mapping.getId())
                .userId(mapping.getUser() != null ? mapping.getUser().getId() : null)
                .roleId(mapping.getRole() != null ? mapping.getRole().getId() : null)
                .storeId(mapping.getStore() != null ? mapping.getStore().getId() : null)
                .branchId(mapping.getBranch() != null ? mapping.getBranch().getId() : null)
                .assignedById(mapping.getAssignedBy() != null ? mapping.getAssignedBy().getId() : null)
                .build();
    }

    // DTO → Entity
    public static UserRoleMapping toEntity(UserRoleMappingDto dto, User user, Role role, Store store, Branch branch, User assignedBy) {
        if (dto == null) return null;
        return UserRoleMapping.builder()
                .id(dto.getId())
                .user(user)
                .role(role)
                .store(store)
                .branch(branch)
                .assignedBy(assignedBy)
                .build();
    }
}
