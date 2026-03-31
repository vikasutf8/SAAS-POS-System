package com.pm.saaspossystem.payload.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchDto {
    private Long id;

    @NotBlank(message = "Branch name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 300)
    private String address;

    @NotBlank(message = "Phone is required")
    @Size(max = 20)
    private String phone;

    @Email
    private String email;

    private List<String> workingDays;

    @NotNull(message = "Open time is required")
    private LocalTime openTime;

    @NotNull(message = "Close time is required")
    private LocalTime closeTime;

    /**
     * On request  → required, which store this branch belongs to
     * On response → populated
     */
    @NotNull(message = "Store id is required")
    private Long storeId;

    /**
     * On request  → not sent, resolved from SecurityContext
     * On response → who created this branch
     */
    private Long createdById;

    /**
     * On request  → optional, assign manager at creation time
     * On response → assigned branch manager id
     */
    private Long branchManagerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
