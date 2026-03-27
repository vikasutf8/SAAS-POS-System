package com.pm.saaspossystem.seeder;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.model.Role;
import com.pm.saaspossystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
    }

    private void seedRoles() {
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();
                roleRepository.save(role);
                log.info("✅ Seeded role: {}", roleName);
            } else {
                log.debug("⏭️ Role already exists, skipping: {}", roleName);
            }
        }
    }
}
