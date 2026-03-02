package com.pm.saaspossystem.payload.response;

import com.pm.saaspossystem.payload.dto.UserDto;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthResponse {

    private  String jwt;
    private  String message;
    private UserDto user;
}
