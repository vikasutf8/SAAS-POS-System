package com.pm.saaspossystem.services;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.CategoryDto;

import java.util.List;

public interface CategoryServices {

    CategoryDto createCategory(CategoryDto categoryDto) throws UserExceptions;
    List<CategoryDto> getCategoriesByStore(Long storeId);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws UserExceptions;
    void deleteCategory(Long id) throws UserExceptions;
}
