package com.pm.saaspossystem.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shift_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime shiftStart;

    private LocalDateTime shiftEnd;

    private Double totalSales;

    // total sales - refunds
    private Double netSales;

    private Integer totalOrders;

    // Many shifts can belong to one cashier
    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;


    @OneToMany(mappedBy = "shiftReport")
    private List<Refund> refunds;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
