package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.payload.dto.ProductDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.services.ProductServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServices productServices;
    private final UserService userService;

    // =========================================
    // Create Product
    // =========================================
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) throws UserExceptions {

        UserDto currentUser = userService.getCurrentUser();

        return productServices.createProduct(productDto, UserMapper.toEntity(currentUser));
    }

    // =========================================
    // Update Product
    // =========================================
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                    @RequestBody ProductDto productDto) throws UserExceptions {

        UserDto currentUser = userService.getCurrentUser();

        return productServices.updateProduct(id, productDto, UserMapper.toEntity(currentUser));
    }

    // =========================================
    // Delete Product
    // =========================================
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) throws UserExceptions {

        UserDto currentUser = userService.getCurrentUser();

        productServices.deleteProduct(id, UserMapper.toEntity(currentUser));
    }

    // =========================================
    // Get All Products By Store
    // =========================================
    @GetMapping("/store/{storeId}")
    public List<ProductDto> getAllProductsByStore(@PathVariable Long storeId) {

        return productServices.getAllProductsByStoreId(storeId);
    }

    // =========================================
    // Search Products
    // =========================================
    @GetMapping("/search")
    public List<ProductDto> searchProducts(
            @RequestParam Long storeId,
            @RequestParam String keyword) {

        return productServices.searchByKeyword(storeId, keyword);
    }
}
