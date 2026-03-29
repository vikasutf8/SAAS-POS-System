package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMappingDto {

    private Long id;

    private RoleName roleName;

    /** Populated for STORE_MANAGER */
    private Long storeId;

    /** Populated for BRANCH_MANAGER and BRANCH_CASHIER */
    private Long branchId;

    private Long assignedById;
//    private String assignedByName;   // handy for display — saves a second call

//    private LocalDateTime assignedAt;
//    private LocalDateTime updatedAt;
}
