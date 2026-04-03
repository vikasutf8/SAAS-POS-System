package com.pm.saaspossystem.payload.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private Long id;
    @NotBlank(message = "Branch is required")
    private Long branchId;
    @NotBlank(message = "Product is required")
    private Long productId;

    private Integer quantity;

}
