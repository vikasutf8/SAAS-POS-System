package com.pm.saaspossystem.model;

import com.pm.saaspossystem.domain.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "roles",
        indexes = {
                @Index(name = "idx_role_name", columnList = "name")
        }
        )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Use a proper enum column.
     * Values: ADMIN, STORE_MANAGER, BRANCH_MANAGER, BRANCH_CASHIER
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;
}
