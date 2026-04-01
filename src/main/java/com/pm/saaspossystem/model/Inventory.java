package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventories",
        indexes = {
                @Index(name = "idx_inventory_branch", columnList = "branch_id"),
                @Index(name = "idx_inventory_product", columnList = "product_id")
        }

)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    // ========================================
    // ID
    // ========================================
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false) //fk unidirectional owning side
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)//fk unidirectional owning side
    private Product product;

    // ========================================
    // STOCK
    // ========================================
    @Column(nullable = false)
    private Integer quantity;

    // ========================================
    // AUDIT FIELDS
    // ========================================

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    // ========================================
    // LIFECYCLE CALLBACKS
    // ========================================

    @PrePersist
    public void prePersist() {
//        this.lastUpdated = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        if (this.quantity == null) {
            this.quantity = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

}
