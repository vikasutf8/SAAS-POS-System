package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.CategoryDto;
import com.pm.saaspossystem.services.CategoryServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/categories")
@PreAuthorize("hasRole('ADMIN') or hasRole('STORE_MANAGER')")
public class CategoryController {
    private final CategoryServices categoryServices;
    private final UserService userService;

    // =========================================
    // Create Category
    // =========================================
    @PostMapping

    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto)
            throws UserExceptions, IllegalAccessException {

        return categoryServices.createCategory(categoryDto);
    }

    // =========================================
    // Get Categories By Store
    // =========================================
    @GetMapping("/store/{storeId}")
    public List<CategoryDto> getCategoriesByStore(@PathVariable Long storeId) {

        return categoryServices.getCategoriesByStore(storeId);
    }

    // =========================================
    // Update Category
    // =========================================
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody CategoryDto categoryDto)
            throws UserExceptions, IllegalAccessException {

        return categoryServices.updateCategory(id, categoryDto);
    }

    // =========================================
    // Delete Category
    // =========================================
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id)
            throws UserExceptions, IllegalAccessException {

        categoryServices.deleteCategory(id);
    }
}
