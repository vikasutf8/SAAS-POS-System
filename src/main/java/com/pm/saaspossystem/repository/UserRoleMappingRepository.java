package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {

     UserRoleMapping findByUserId(Long userId);
}
