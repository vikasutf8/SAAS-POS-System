package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.domain.RoleName;
import com.pm.saaspossystem.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {

     UserRoleMapping findByUserId(Long userId);

//     boolean existsByUserIdAndRoleName(Long userId, RoleName roleName);
//
//    UserRoleMapping findByUserIdAndRoleName(Long userId, RoleName roleName);

    Long countByUserIdAndRoleName(Long userId, RoleName roleName);

    UserRoleMapping findByUserIdAndStoreId(Long userId, Long storeId);
}
