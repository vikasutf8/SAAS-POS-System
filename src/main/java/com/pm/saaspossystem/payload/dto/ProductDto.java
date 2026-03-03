package com.pm.saaspossystem.payload.dto;

import java.time.LocalDateTime;

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

    // Many products belong to one category
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    @NotNull(message = "Category is required")
//    private Category category;

    // Many products belong to one store
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id", nullable = false)
//    @NotNull(message = "Store is required")
    private Long storeId;
    private Long categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
