package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.StoreStatus;
import com.pm.saaspossystem.model.StoreContact;
import com.pm.saaspossystem.model.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class StoreDto {

    private Long id;

    // Branch Name
    @NotBlank(message = "Branch name is required")
    private String branch;

    @NotBlank(message = "Brand name is required")
    private String brand;

    // Store Admin (One-to-One)
    private User storeAdmin;

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
