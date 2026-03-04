package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    List<Inventory> findByProductId(Long productId);
    List<Inventory> findByBranchId(Long branchId);
    Optional<Inventory> findByProductIdAndBranchId(Long productId, Long branchId);
}
