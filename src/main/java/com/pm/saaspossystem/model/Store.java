package com.pm.saaspossystem.model;

import com.pm.saaspossystem.domain.StoreStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "stores",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_code",
                        columnNames = {"storeCode"}
                )
        },
        indexes = {
                @Index(name = "idx_store_code", columnList = "storeCode"),
                @Index(name = "idx_store_manager", columnList = "store_manager_id"),
                @Index(name = "idx_created_by", columnList = "created_by_id")
        }

)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String storeCode;

    @NotBlank
    @Column(nullable = false)
    private String brand;

    @Column(length = 500)
    private String description;

    @NotBlank
    private String storeType; // TODO: convert to enum later

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    @Embedded
    @Valid
    private StoreContact contact;

    /**
     * The Admin who created this store.
     * Many stores can be created by one admin (but in practice only one admin exists).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    @NotNull(message = "Created by (admin) is required")
    private User createdBy;

    /**
     * The assigned Store Manager for this store.
     * Unique: one user can manage only one store.
     * Nullable: store may not have a manager yet.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_manager_id", unique = true)
    private User storeManager;

    /**
     * All branches under this store.
     */
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Branch> branches = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = StoreStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
