package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v2/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() throws UserExceptions {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // ✅ Get user from JWT (if needed manually)
    @GetMapping("/token")
    public ResponseEntity<UserDto> getUserFromToken(
            @RequestHeader("Authorization") String header)
            throws UserExceptions {

        String token = header.substring(7); // remove "Bearer "
        return ResponseEntity.ok(userService.getUserFromJwt(token));
    }

//    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id)
            throws UserExceptions {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ✅ Get user by email (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
            throws UserExceptions {

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // ✅ Get all users (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PatchMapping("/update")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UserDto userDto, Long id) throws UserExceptions  {

        return ResponseEntity.ok(userService.updateUser(id,userDto));
    }



}
