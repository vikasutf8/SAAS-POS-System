package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.StoreStatus;
import jakarta.validation.constraints.NotBlank;
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


    @NotBlank(message = "Brand name is required")
    private String brand;

    // Store Admin (One-to-One)
    private UserDto storeAdmin; // userId of the store admin

    // Store Type
    @NotBlank(message = "Store type is required")
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
