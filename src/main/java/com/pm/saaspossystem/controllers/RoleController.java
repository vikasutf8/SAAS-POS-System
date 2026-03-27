package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.mapper.RoleMapper;
import com.pm.saaspossystem.payload.dto.RoleDto;
import com.pm.saaspossystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role")
public class RoleController {

    private final RoleRepository roleRepository;

    /**
     * GET api/v1/role
     * Returns all seeded roles.
     * Useful for frontend dropdowns when assigning roles.
     */
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }

    /**
     * GET api/v1/role/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(RoleMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET api/v1/role/name/{name}
     * e.g. /api/v1/role/name/BRANCH_MANAGER
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDto> getRoleByName(@PathVariable RoleName name) {
        return roleRepository.findByName(name)
                .map(RoleMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
//```
//
//        ---
//
//        ### Full picture
//```
//startup
//  └── RoleSeeder.run()
//        └── inserts ADMIN, STORE_MANAGER, BRANCH_MANAGER, BRANCH_CASHIER if missing
//
//api/v1/role          GET → all roles (list)
//api/v1/role/{id}     GET → by id
//api/v1/role/name/{}  GET → by RoleName enum value