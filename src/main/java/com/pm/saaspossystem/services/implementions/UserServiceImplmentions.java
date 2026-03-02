package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.configurations.JwtProvider;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplmentions implements UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public UserDto getUserFromJwt(String token) throws UserExceptions {

        if (token == null || token.isBlank()) {
            throw new UserExceptions("Token is missing");
        }

        // ✅ Validate & extract email
        String email = jwtProvider.extractEmailFromToken(token);

        if (email == null || email.isBlank()) {
            throw new UserExceptions("Invalid token");
        }

        // ✅ Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("Invalid or expired token"));

        // ✅ Map entity -> DTO

        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .lastLogin(user.getLastLogin())
                .build();

    }

    @Override
    public UserDto getCurrentUser() throws UserExceptions {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserExceptions("User not authenticated");
        }

        String email = authentication.getName(); // this is your username (email)

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));

        return mapToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) throws UserExceptions {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));

        return mapToDto(user);
    }

    @Override
    public UserDto getUserById(Long id) throws UserExceptions {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserExceptions("User not found"));

        return mapToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToDto)
                .toList();
    }


    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .store(user.getStore())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
