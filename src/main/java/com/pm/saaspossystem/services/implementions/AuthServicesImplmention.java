package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.configurations.JwtProvider;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.payload.response.AuthResponse;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.services.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServicesImplmention implements AuthServices {


    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImplmention customUserServiceImplmention;
    @Override
    public AuthResponse signup(UserDto userDto) throws UserExceptions {

        // ✅ 1. Check if user already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserExceptions(STR."User already exists with email: \{userDto.getEmail()}");
        }

        // ✅ 2. Prevent manual ADMIN creation (important security rule)
//        if (userDto.getRole() == UserRole.ROLE_ADMIN) {
//            throw new UserExceptions("Admin registration is not allowed.");
//        }

        // ✅ 3. Create new user
        User newUser = UserMapper.toEntity(userDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));


        User savedUser = userRepository.save(newUser);

        // ✅ 4. Create Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                savedUser.getPassword(),
                List.of(new SimpleGrantedAuthority( savedUser.getRole().name()))
        );

        // ✅ 5. Set Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ 6. Generate JWT
        String token = jwtProvider.generateAccessToken(authentication);

        // ✅ 7. Map to DTO (manual for now)
        UserDto responseUser = UserMapper.toDto(savedUser);

        // ✅ 8. Return AuthResponse
        return AuthResponse.builder()
                .jwt(token)
                .message("User registered successfully")
                .user(responseUser)
                .build();
    }
    @Override
    public AuthResponse login(UserDto userDto) throws UserExceptions {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        // ✅ 1. Authenticate user
        Authentication authentication = authenticate(email, password);

        // ✅ 2. Set security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ 3. Generate JWT
        String token = jwtProvider.generateAccessToken(authentication);

        // ✅ 4. Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));

        // ✅ 5. Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // ✅ 6. Map to DTO (without password!)
        UserDto responseUser = UserMapper.toDto(user);

        // ✅ 7. Return response
        return AuthResponse.builder()
                .jwt(token)
                .message("Login successful")
                .user(responseUser)
                .build();

    }

    private Authentication authenticate(String email, String password) {
   //verify password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid email or password"));

        // ✅ Verify password using BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // ✅ Create authorities
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority( user.getRole().name())
        );

        // ✅ Return authenticated object
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
