package com.pm.saaspossystem.mapper;

//public class StoreMapper {
//
//    private StoreMapper() {
//        // prevent instantiation
//    }
//
//    // ================================
//    // Entity → DTO
//    // ================================
//
//    public static StoreDto toDto(Store store) {
//        if (store == null) {
//            return null;
//        }
//
//        return StoreDto.builder()
//                .id(store.getId())
//                .brand(store.getBrand())
//                .storeType(store.getStoreType())
//                .description(store.getDescription())
//                .status(store.getStatus())
////                .storeAdminId(store.getStoreAdmin() != null ? store.getStoreAdmin().getId() : null)
//                .contact(toContactDto(store.getContact()))
//                .createdAt(store.getCreatedAt())
//                .updatedAt(store.getUpdatedAt())
//                .build();
//    }
//
//    // ================================
//    // DTO → Entity
//    // ================================
//
//    public static Store toEntity(StoreDto dto, User storeAdmin) {
//        if (dto == null) {
//            return null;
//        }
//
//        return Store.builder()
//                .id(dto.getId())
//                .brand(dto.getBrand())
//                .storeCode()
//                .storeType(dto.getStoreType())
//                .description(dto.getDescription())
//                .status(dto.getStatus()) // usually ignored on createfd

import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.StoreContact;
import com.pm.saaspossystem.payload.dto.StoreContactDto;
import com.pm.saaspossystem.payload.dto.StoreDto;

////                .storeAdmin(storeAdmin)
//                .contact(toContactEntity(dto.getContact()))
//                .createdAt(dto.getCreatedAt())
//                .updatedAt(dto.getUpdatedAt())
//                .build();
//    }
//
//    // ================================
//    // Contact Mapping
//    // ================================
//
//    public static StoreContactDto toContactDto(@Valid StoreContact contact) {
//        if (contact == null) {
//            return null;
//        }
//
//        return StoreContactDto.builder()
//                .address(contact.getAddress())
//                .phone(contact.getPhone())
//                .email(contact.getEmail())
//                .build();
//    }
//
//    public static StoreContact toContactEntity(StoreContactDto dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        return StoreContact.builder()
//                .address(dto.getAddress())
//                .phone(dto.getPhone())
//                .email(dto.getEmail())
//                .build();
//    }
//}


public class StoreMapper {

    private StoreMapper() {}

    // ─────────────────────────────────────────
    // Entity → DTO
    // ─────────────────────────────────────────

    public static StoreDto toDto(Store store) {
        if (store == null) return null;

        return StoreDto.builder()
                .id(store.getId())
                .storeCode(store.getStoreCode())
                .brand(store.getBrand())
                .description(store.getDescription())
                .storeType(store.getStoreType())
                .status(store.getStatus())
                .contact(toContactDto(store.getContact()))
                .createdById(
                        store.getCreatedBy() != null ? store.getCreatedBy().getId() : null
                )
                .storeManagerId(
                        store.getStoreManager() != null ? store.getStoreManager().getId() : null
                )
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .build();
    }

    // ─────────────────────────────────────────
    // DTO → Entity  (creation only)
    // ─────────────────────────────────────────

    /**
     * Builds a bare Store entity.
     * createdBy and storeManager are NOT set here —
     * resolved in service layer from SecurityContext / repo lookup.
     */
    public static Store toEntity(StoreDto dto) {
        if (dto == null) return null;

        return Store.builder()
                .storeCode(dto.getStoreCode())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .storeType(dto.getStoreType())
                .contact(toContactEntity(dto.getContact()))
                // status     → auto PENDING via @PrePersist
                // createdBy  → set in service
                // storeManager → set in service if storeManagerId present
                .build();
    }

    // ─────────────────────────────────────────
    // Update Existing Entity (PATCH-style)
    // ─────────────────────────────────────────

    public static void updateEntity(Store store, StoreDto dto) {
        if (store == null || dto == null) return;

        if (dto.getBrand() != null)       store.setBrand(dto.getBrand());
        if (dto.getDescription() != null) store.setDescription(dto.getDescription());
        if (dto.getStoreType() != null)   store.setStoreType(dto.getStoreType());
        if (dto.getStatus() != null)      store.setStatus(dto.getStatus());
        if (dto.getContact() != null)     store.setContact(toContactEntity(dto.getContact()));
        // storeCode changes → own verified flow (it's unique, treat carefully)
    }

    // ─────────────────────────────────────────
    // StoreContact helpers
    // ─────────────────────────────────────────

    private static StoreContactDto toContactDto(StoreContact contact) {
        if (contact == null) return null;

        return StoreContactDto.builder()
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .address(contact.getAddress())
                .city(contact.getCity())
                .pincode(contact.getPincode())
                .build();
    }

    private static StoreContact toContactEntity(StoreContactDto dto) {
        if (dto == null) return null;

        return StoreContact.builder()
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .city(dto.getCity())
                .pincode(dto.getPincode())
                .build();
    }
}