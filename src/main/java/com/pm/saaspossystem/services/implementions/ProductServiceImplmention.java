package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.ProductDto;
import com.pm.saaspossystem.services.ProductServices;

import java.util.List;

public class ProductServiceImplmention implements ProductServices {
    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {
        return null;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) {
        return null;
    }

    @Override
    public void deleteProduct(Long id, User user) {

    }

    @Override
    public List<ProductDto> getAllProductsByStoreId(Long storeId) {
        return List.of();
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        return List.of();
    }
}
