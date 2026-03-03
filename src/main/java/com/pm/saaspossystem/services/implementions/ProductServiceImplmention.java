package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.ProductDto;
import com.pm.saaspossystem.repository.ProductRepository;
import com.pm.saaspossystem.services.ProductServices;
import com.pm.saaspossystem.services.StoreServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImplmention implements ProductServices {

    private final ProductRepository productRepository;
    private final StoreServices storeServices;



    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {
//        Store store = storeRepository.findById(dto.getStoreId())
//                .orElseThrow(() -> new RuntimeException("Store not found"));
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        Product product = ProductMapper.toEntity(dto, store, category);
//
//        productRepository.save(product);


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
