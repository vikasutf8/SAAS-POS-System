package com.pm.saaspossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;

    private String name;

    private String sku;

    private String description;

    private Double mrp;

    private Double sellingPrice;

    private String brand;

    private String imageUri;

    // ========================================
    // Relationships
    // ========================================

    private Long storeId;
    private Long categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



}
