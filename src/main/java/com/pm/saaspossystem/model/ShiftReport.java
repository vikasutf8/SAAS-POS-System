package com.pm.saaspossystem.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shift_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToOne
    @JoinColumn(name="branch_id")
    private Branch branch;

    @Transient
    private List<PaymentSummery> paymentSummeries;


    @OneToMany
    private List<Product> topSellingProducts;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Order> recentOrders;




    @OneToMany(mappedBy = "shiftReport", cascade = CascadeType.ALL) // mapped by donot create a sperate table
    private List<Refund> refunds;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private LocalDateTime shiftEnd;


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
