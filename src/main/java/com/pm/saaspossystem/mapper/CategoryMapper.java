package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Category;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.payload.dto.CategoryDto;

public class CategoryMapper {

    private CategoryMapper() {
        // prevent instantiation
    }

    // =========================================
    // Entity → DTO
    // =========================================
    public static CategoryDto toDto(Category category) {

        if (category == null) {
            return null;
        }

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(
                        category.getStore() != null
                                ? category.getStore().getId()
                                : null
                )
                .build();
    }

    // =========================================
    // DTO → Entity (For Create)
    // =========================================
    public static Category toEntity(CategoryDto dto, Store store) {

        if (dto == null) {
            return null;
        }

        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .store(store)
                .build();
    }

    // =========================================
    // Update Existing Entity
    // =========================================
    public static void updateEntity(Category category,
                                    CategoryDto dto,
                                    Store store) {

        if (category == null || dto == null) {
            return;
        }

        category.setName(dto.getName());

        if (store != null) {
            category.setStore(store);
        }
    }
}
