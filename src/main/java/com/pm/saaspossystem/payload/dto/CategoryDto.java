package com.pm.saaspossystem.payload.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    private Long id;
    @NotBlank(message = "Category name is required")
    private String name;
    @NotBlank(message = "Store is required")
    private Long storeId;

}

