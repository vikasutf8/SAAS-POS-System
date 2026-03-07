package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.domain.UserRole;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    List<User> findByStoreAndRoleIn(Store store, List<UserRole> roleStoreManager);

    List<User> findByStore(Store store);
    List<User> findByBranchId(Long branchId);
}
