package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Category;
import com.pm.saaspossystem.model.Product;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.ProductDto;

public class ProductMapper {

    private ProductMapper() {
        // prevent instantiation
    }

    // =========================================
    // Entity → DTO
    // =========================================
    public static ProductDto toDto(Product product) {

        if (product == null) {
            return null;
        }

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .mrp(product.getMrp())
                .sellingPrice(product.getSellingPrice())
                .brand(product.getBrand())
                .imageUri(product.getImageUri())
                .storeId(
                        product.getStore() != null
                                ? product.getStore().getId()
                                : null
                )
                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )

                .build();
    }

    // =========================================
    // DTO → Entity (For Create)
    // =========================================
    public static Product toEntity(ProductDto dto,
                                   Store store,
                                   Category category
    ) {

        if (dto == null) {
            return null;
        }

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .sku(dto.getSku())
                .description(dto.getDescription())
                .mrp(dto.getMrp())
                .sellingPrice(dto.getSellingPrice())
                .brand(dto.getBrand())
                .imageUri(dto.getImageUri())
                .store(store)
                .category(category)
                .build();
    }

    // =========================================
    // Update Existing Entity
    // =========================================
    public static void updateEntity(Product product,
                                    ProductDto dto,
                                    Store store,
                                    Category category
    )
    {

        if (product == null || dto == null) {
            return;
        }

        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setDescription(dto.getDescription());
        product.setMrp(dto.getMrp());
        product.setSellingPrice(dto.getSellingPrice());
        product.setBrand(dto.getBrand());
        product.setImageUri(dto.getImageUri());

        if (store != null) {
            product.setStore(store);
        }

        if (category != null) {
            product.setCategory(category);
        }
    }
}
