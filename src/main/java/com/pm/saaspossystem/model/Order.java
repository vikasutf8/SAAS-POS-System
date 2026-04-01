package com.pm.saaspossystem.model;

import com.pm.saaspossystem.domain.OrderStatus;
import com.pm.saaspossystem.domain.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double totalAmount;

    private LocalDateTime createdAt;

    // Many Orders -> One Branch
    @ManyToOne
    @JoinColumn(name = "branch_id")//fk unidirectional owning side
    private Branch branch;

    // Many Orders -> One Cashier(User)
    @ManyToOne
    @JoinColumn(name = "cashier_id")//fk unidirectional owning side
    private User cashier;

    // Many Orders -> One Customer
    @ManyToOne
    @JoinColumn(name = "customer_id")//fk unidirectional owning side
    private Customer customer;

    // Enum Payment Type
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



    // One Order -> Many OrderItems
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

//    public OrderStatus getOrderStatus() {
//    }
}
