package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName name);
    List<Role> findAllByNameIn(List<RoleName> names);
    boolean existsByName(RoleName name);
}
