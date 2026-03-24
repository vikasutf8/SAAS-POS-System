package com.pm.saaspossystem.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    private Long id;
    private String name;
    private Long storeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

