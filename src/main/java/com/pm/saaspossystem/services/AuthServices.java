package com.pm.saaspossystem.services;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.payload.request.LoginRequestDto;
import com.pm.saaspossystem.payload.response.AuthResponse;

public interface AuthServices {

    AuthResponse signup(UserDto userDto) throws UserExceptions;
    AuthResponse login(LoginRequestDto userDto) throws UserExceptions;
}

