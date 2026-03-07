package com.pm.saaspossystem.payload.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreContactDto {

    private String address;
    private String phone;
    private String email;
}
