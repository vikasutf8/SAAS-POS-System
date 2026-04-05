package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    private Long id;



    private String brand;
    private String storeCode;
    // Store Admin (One-to-One)
    private UserDto storeAdmin; // userId of the store admin

    // Store Type

    private String storeType;

    // Description
    private String description;

    // Status
    private StoreStatus status;

    // Embedded Contact
    private StoreContactDto contact;

    // Timestamps
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
