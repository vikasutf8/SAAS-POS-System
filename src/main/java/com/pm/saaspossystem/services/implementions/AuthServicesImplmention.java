package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.configurations.JwtProvider;
import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Role;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.model.UserRoleMapping;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.payload.response.AuthResponse;
import com.pm.saaspossystem.repository.RoleRepository;
import com.pm.saaspossystem.repository.UserRepository;
import com.pm.saaspossystem.repository.UserRoleMappingRepository;
import com.pm.saaspossystem.services.AuthServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServicesImplmention implements AuthServices {


    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImplmention customUserServiceImplmention;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    @Override
    public AuthResponse signup(UserDto userDto) throws UserExceptions {
/**8
 * {
 *   "fullName": "Ravi Mehta",
 *   "password": "StorePass@456",
 *   "phone": "9123456780",
 *   "email": "ravi@saaspos.com",
 *   "roleName": ["STORE_MANAGER","BRANCH_MANAGER","CASHIER"]
 *
 * }
 */
        // 1. Check if user already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserExceptions(STR."User already exists with email: \{userDto.getEmail()}");
        }

        // 2. Role Validation & Fetching
        List<Role> roles = validateAndFetchRoles(userDto.getRoles());
        log.info("Validated roles for signup: {}", roles.stream().map(Role::getName).toList());
/*
[
  Role { id=2, name=STORE_MANAGER },
  Role { id=3, name=BRANCH_MANAGER }
]
 */
        //  3. Create new user
        User newUser = UserMapper.toEntity(userDto);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoleNames(userDto.getRoles().stream().map(roleName -> roleName).toList());

        log.info("Creating user with roles: {}", newUser.getRoleNames());

        User savedUser = userRepository.save(newUser);

        // 4. store userRoleMapping
        for (Role role : roles) {
            UserRoleMapping mapping = UserRoleMapping.builder()
                    .user(savedUser)
                    .role(role)
                    .assignedBy(savedUser) // assigner
                    .build();

            userRoleMappingRepository.save(mapping);
        }

        // 5. Create Authentication object
        Authentication authentication = buildAuthentication(savedUser);

        // 6. Set Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 7 Generate JWT
        String token = jwtProvider.generateAccessToken(authentication);

        // 8 Map to DTO (manual for now)
        UserDto responseUser = UserMapper.toDto(savedUser);

        // 9 Return AuthResponse
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

        log.info("Login attempt for email: {}", email);
        // 1. Verify credentials → throws if wrong email/password
        Authentication authentication = authenticate(userDto.getEmail(), userDto.getPassword());

        // 2. Set security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate JWT
        String token = jwtProvider.generateAccessToken(authentication);

        // 4. Update lastLogin
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserExceptions("User not found"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponse.builder()
                .jwt(token)
                .message("Login successful")
                .user(UserMapper.toDto(user))
                .build();

    }



    private Authentication buildAuthentication(User user) {

        // Each role → its own GrantedAuthority  (fixes the List.toString() bug)
        List<GrantedAuthority> authorities = user.getRoleNames().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
    private Authentication authenticate(String email, String password) {

        // 1. Find user or throw
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        // 2. BCrypt password check
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return buildAuthentication(user);
    }

    private List<Role> validateAndFetchRoles(List<RoleName> roleNames) {

        // 1. ADMIN must be alone
        if (roleNames.contains(RoleName.ADMIN) && roleNames.size() > 1) {
            throw new IllegalArgumentException("ADMIN cannot be combined with other roles");
        }

        // 2. Single DB call — fetch all at once
        List<Role> foundRoles = roleRepository.findAllByNameIn(roleNames);

        // 3. Detect any invalid role names
        if (foundRoles.size() != roleNames.size()) {
            List<RoleName> foundNames = foundRoles.stream()
                    .map(Role::getName)
                    .toList();

            List<RoleName> invalidRoles = roleNames.stream()
                    .filter(r -> !foundNames.contains(r))
                    .toList();

            throw new IllegalArgumentException("Invalid roles: " + invalidRoles);
        }

        return foundRoles;
    }
}
