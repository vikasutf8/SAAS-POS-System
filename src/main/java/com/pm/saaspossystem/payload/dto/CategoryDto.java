package com.pm.saaspossystem.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CategoryDto {


    private Long id;
    private String name;
    private Long storeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

