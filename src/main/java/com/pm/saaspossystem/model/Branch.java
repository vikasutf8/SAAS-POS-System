package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
        name = "branches",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_branch_name_store",
                        columnNames = {"name", "store_id"}
                )
        },
        indexes = {
                @Index(name = "idx_branch_store", columnList = "store_id"),
                @Index(name = "idx_branch_manager", columnList = "branch_manager_id")
        }
)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank @Size(max = 150)
    @Column(nullable = false)
    private String name;

    @NotBlank @Size(max = 300)
    @Column(nullable = false)
    private String address;

    @NotBlank @Size(max = 20)
    @Column(nullable = false)
    private String phone;

    @Email
    private String email;

    @ElementCollection
    @CollectionTable(name = "branch_working_days", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "day")
    private List<String> workingDays = new ArrayList<>();

    @NotNull
    private LocalTime openTime;

    @NotNull
    private LocalTime closeTime;

    /**
     * The store this branch belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull
    private Store store;


    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE) // as branch manager is user only one branch else no use
    @JoinColumn(name = "branch_manager_id")
    private User branchManager;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
