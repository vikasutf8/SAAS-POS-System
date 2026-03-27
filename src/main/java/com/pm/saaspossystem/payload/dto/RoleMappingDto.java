package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMappingDto {

    private Long id;

    private Role.RoleName roleName;

    /** Populated for STORE_MANAGER */
    private Long storeId;

    /** Populated for BRANCH_MANAGER and BRANCH_CASHIER */
    private Long branchId;

    private Long assignedById;
    private String assignedByName;   // handy for display — saves a second call

    private LocalDateTime assignedAt;
}
