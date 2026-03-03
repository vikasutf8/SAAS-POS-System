package com.pm.saaspossystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table( name = "categories"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "uk_product_sku", columnNames = "sku")
//        }
        )
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Category {
    // ========================================
    // ID
    // ========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
