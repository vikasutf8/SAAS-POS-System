package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.configurations.JwtProvider;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

        return UserMapper.toDto(user);

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
log.info(STR."current user email \{user}");
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) throws UserExceptions {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long id) throws UserExceptions {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserExceptions("User not found"));

        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) throws UserExceptions {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserExceptions("User not found"));

//       existingUser.setStore(userDto.getStoreId());
        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toDto(updatedUser);
    }



}
