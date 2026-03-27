package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_role_mappings",
        uniqueConstraints = {
                // Prevents duplicate role assignment in same context
                @UniqueConstraint(
                        name = "uk_user_role_branch",
                        columnNames = {"user_id", "role_id", "branch_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_role_user", columnList = "user_id"),
                @Index(name = "idx_user_role_role", columnList = "role_id"),
                @Index(name = "idx_user_role_branch", columnList = "branch_id"),
                @Index(name = "idx_user_role_store", columnList = "store_id"),
                @Index(name = "idx_user_role_assigned_by", columnList = "assigned_by_id"),
                @Index(name = "idx_user_role_assigned_at", columnList = "assignedAt")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull
    private Role role;

    /**
     * Set for STORE_MANAGER role.
     * Null for ADMIN.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * Set for BRANCH_MANAGER and BRANCH_CASHIER roles.
     * One user can have max 2 rows with BRANCH_MANAGER role (enforced in service).
     * One user can also have BRANCH_CASHIER rows for their own branches.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    /** Who assigned this role — Admin or Store Manager. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_id", nullable = false)
    @NotNull
    private User assignedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onAssign() {
        this.assignedAt = LocalDateTime.now();
    }
}
