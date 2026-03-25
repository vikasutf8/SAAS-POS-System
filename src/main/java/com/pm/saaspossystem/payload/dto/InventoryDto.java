package com.pm.saaspossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private Long id;

    private Long branchId;

    private Long productId;

    private Integer quantity;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdated;

}
