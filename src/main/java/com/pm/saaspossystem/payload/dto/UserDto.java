package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    /** Only present on create/update requests — never returned in responses */
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private boolean isActive;

    /** All role assignments — each with its own store/branch context */
    private List<RoleName> roles;

    private Long storeId; // for STORE_MANAGER role
//
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private LocalDateTime lastLogin;
}
