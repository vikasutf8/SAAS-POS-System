package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    @NotNull(message = "Role name is required")
    private RoleName name;   // was Role.RoleName
}
