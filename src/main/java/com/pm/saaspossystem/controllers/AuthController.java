package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.payload.response.AuthResponse;
import com.pm.saaspossystem.services.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/auth")
public class AuthController {

    private final AuthServices authService;

    // ✅ Signup Endpoint
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody UserDto userDto) throws UserExceptions {

        AuthResponse response = authService.signup(userDto);
        return ResponseEntity.ok(response);
    }

    // ✅ Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserDto userDto) throws UserExceptions {

        AuthResponse response = authService.login(userDto);
        return ResponseEntity.ok(response);
    }
}
