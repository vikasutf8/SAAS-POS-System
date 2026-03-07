package com.pm.saaspossystem.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class InventoryDto {

    private Long id;

    private Long branchId;

    private Long productId;

    private Integer quantity;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdated;

}
