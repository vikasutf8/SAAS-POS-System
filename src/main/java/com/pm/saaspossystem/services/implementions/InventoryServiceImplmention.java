package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.mapper.InventoryMapper;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Inventory;
import com.pm.saaspossystem.model.Product;
import com.pm.saaspossystem.payload.dto.InventoryDto;
import com.pm.saaspossystem.repository.BranchRepository;
import com.pm.saaspossystem.repository.InventoryRepository;
import com.pm.saaspossystem.repository.ProductRepository;
import com.pm.saaspossystem.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImplmention implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) {
        Branch branch = branchRepository.findById(inventoryDto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // 2️⃣ Validate product
        Product product = productRepository.findById(inventoryDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3️⃣ Prevent duplicate inventory
//        inventoryRepository
//                .findByProductIdAndBranchId(product.getId(), branch.getId())
//                .ifPresent(inv -> {
//                    throw new RuntimeException("Inventory already exists for this product in this branch");
//                });

        // 4️⃣ Create entity
        Inventory inventory =
                InventoryMapper.toEntity(inventoryDto, branch, product);

        Inventory saved = inventoryRepository.save(inventory);

        return InventoryMapper.toDto(saved);
    }

    @Override
    public InventoryDto updateInventory(Long id, InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        // Optional: Only update quantity
        if (inventoryDto.getQuantity() != null) {

            if (inventoryDto.getQuantity() < 0) {
                throw new RuntimeException("Quantity cannot be negative");
            }

            inventory.setQuantity(inventoryDto.getQuantity());
        }

        Inventory updated = inventoryRepository.save(inventory);

        return InventoryMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteInventory(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryRepository.delete(inventory);
    }

    @Override
    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        return InventoryMapper.toDto(inventory);
    }

    @Override
    public InventoryDto getInventoryByProductIdAndBranchId(Long productId, Long branchId) {
        Inventory inventory = inventoryRepository
                .findByProductIdAndBranchId(productId, branchId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        return InventoryMapper.toDto(inventory);
    }

    @Override
    public List<InventoryDto> getAllInventoryByBranchId(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new RuntimeException("Branch not found");
        }

        return inventoryRepository.findByBranchId(branchId)
                .stream()
                .map(InventoryMapper::toDto)
                .toList();
    }
}
