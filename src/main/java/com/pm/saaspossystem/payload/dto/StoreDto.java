package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.StoreStatus;
import jakarta.validation.Valid;
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

    private Long id;                    // null on request, populated in response

    @NotBlank(message = "Store code is required")
    private String storeCode;

    @NotBlank(message = "Brand is required")
    private String brand;

    private String description;

    @NotBlank(message = "Store type is required")
    private String storeType;

    // ── status is NOT in request — auto-set to PENDING in @PrePersist
    private StoreStatus status;

    @Valid
    private StoreContactDto contact;


    /**
     * On request  → not sent (resolved from SecurityContext in service)
     * On response → admin's id who created this store
     */
    private Long createdById;

    /**
     * On request  → optional, assign manager at creation time
     * On response → id of assigned store manager
     */
    private Long storeManagerId;



    // Timestamps
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
