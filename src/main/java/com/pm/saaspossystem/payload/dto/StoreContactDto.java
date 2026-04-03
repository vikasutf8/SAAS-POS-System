package com.pm.saaspossystem.payload.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreContactDto {

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Address is required")
    private String city;
    @NotBlank(message = "Address is required")
    private String pincode;
}