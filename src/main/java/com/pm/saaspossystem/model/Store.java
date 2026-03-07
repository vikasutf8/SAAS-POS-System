package com.pm.saaspossystem.model;

import com.pm.saaspossystem.domain.StoreStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand name is required")
    @Column(nullable = false)
    private String brand;

    // Store Admin (One-to-One)
    @OneToOne
    @JoinColumn(name = "store_admin_id", nullable = false, unique = true)
    @NotNull(message = "Store admin is required")
    private User storeAdmin;

    // Store Type
    @NotBlank(message = "Store type is required")
    private String storeType;

    // Description
    @Column(length = 500)
    private String description;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    // Embedded Contact
    @Embedded
    @Valid
    private StoreContact contact;

    // Timestamps
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ===============================
    // Lifecycle Hooks
    // ===============================

    @PrePersist
    protected void onCreation() {
        this.createdAt = LocalDateTime.now();
        this.status = StoreStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
