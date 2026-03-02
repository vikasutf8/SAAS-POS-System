package com.pm.saaspossystem.payload.dto;

import com.pm.saaspossystem.domain.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "Password is required")
    @Size(max = 100, min = 8)
    private String password;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;




    @NotNull(message = "Role is required")
    private UserRole role;

    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;
}
