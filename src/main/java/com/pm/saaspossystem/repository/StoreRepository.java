package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {

    Store findByStoreAdminId(Long id);
    Optional<Store> findByStoreCode(String storeCode);
}
