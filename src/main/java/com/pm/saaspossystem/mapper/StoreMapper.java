package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.StoreContact;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.StoreContactDto;
import com.pm.saaspossystem.payload.dto.StoreDto;
import jakarta.validation.Valid;

public class StoreMapper {

    private StoreMapper() {
        // prevent instantiation
    }

    // ================================
    // Entity → DTO
    // ================================

    public static StoreDto toDto(Store store) {
        if (store == null) {
            return null;
        }

        return StoreDto.builder()
                .id(store.getId())
                .brand(store.getBrand())
                .storeType(store.getStoreType())
                .description(store.getDescription())
                .status(store.getStatus())
                .storeAdminId(store.getStoreAdmin() != null ? store.getStoreAdmin().getId() : null)
                .contact(toContactDto(store.getContact()))
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .build();
    }

    // ================================
    // DTO → Entity
    // ================================

    public static Store toEntity(StoreDto dto, User storeAdmin) {
        if (dto == null) {
            return null;
        }

        return Store.builder()
                .id(dto.getId())
                .brand(dto.getBrand())
                .storeType(dto.getStoreType())
                .description(dto.getDescription())
                .status(dto.getStatus()) // usually ignored on create
                .storeAdmin(storeAdmin)
                .contact(toContactEntity(dto.getContact()))
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    // ================================
    // Contact Mapping
    // ================================

    public static StoreContactDto toContactDto(@Valid StoreContact contact) {
        if (contact == null) {
            return null;
        }

        return StoreContactDto.builder()
                .address(contact.getAddress())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }

    public static StoreContact toContactEntity(StoreContactDto dto) {
        if (dto == null) {
            return null;
        }

        return StoreContact.builder()
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .build();
    }
}
