package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public enum RoleName {
        ADMIN,
        STORE_MANAGER,
        BRANCH_MANAGER,
        BRANCH_CASHIER
    }
}
