package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(
        name = "branches",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_branch_name_store",
                        columnNames = {"name", "store_id"}
                )
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Branch {

    // ========================================
    // ID
    // ========================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // ========================================
    // Basic Info
    // ========================================

    @NotBlank(message = "Branch name is required")
    @Size(max = 150)
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 300)
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "Phone is required")
    @Size(max = 20)
    @Column(nullable = false)
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    // ========================================
    // Working Days
    // ========================================

    @ElementCollection
    @CollectionTable(
            name = "branch_working_days",
            joinColumns = @JoinColumn(name = "branch_id")
    )
    @Column(name = "day")
    private List<String> workingDays;

    // ========================================
    // Working Hours
    // ========================================

    @NotNull(message = "Open time is required")
    private LocalTime openTime;

    @NotNull(message = "Close time is required")
    private LocalTime closeTime;

    // ========================================
    // Relationships
    // ========================================

    // Many branches belong to one store
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "Store is required")
    private Store store;

    // One branch has one manager
    @OneToOne
    @JoinColumn(name = "manager_id", unique = true)
    private User manager;

    // ========================================
    // Auditing
    // ========================================

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreation() {
        this.createdAt = LocalDateTime.now();

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
