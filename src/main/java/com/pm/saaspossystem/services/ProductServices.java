package com.pm.saaspossystem.services;

import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.ProductDto;

import java.util.List;

public interface ProductServices {

    ProductDto createProduct(ProductDto productDto, User user);
    ProductDto updateProduct(Long id,ProductDto productDto, User user);
    void deleteProduct(Long id, User user);
    List<ProductDto> getAllProductsByStoreId(Long storeId);
    List<ProductDto> searchByKeyword(Long storeId,String keyword);

}
