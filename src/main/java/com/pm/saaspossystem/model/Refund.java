package com.pm.saaspossystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pm.saaspossystem.domain.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Refund belongs to an Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String reason;

    private Double amount;

    // Refund belongs to a ShiftReport
    @ManyToOne
    @JsonIgnoreProperties
    @JoinColumn(name = "shift_report_id")
    private ShiftReport shiftReport;

    // Cashier who processed refund
    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;

    // Branch where refund happened
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

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
