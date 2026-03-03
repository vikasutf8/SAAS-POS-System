package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.mapper.ProductMapper;
import com.pm.saaspossystem.model.Product;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.ProductDto;
import com.pm.saaspossystem.repository.ProductRepository;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.services.ProductServices;
import com.pm.saaspossystem.services.StoreServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImplmention implements ProductServices {

    private final ProductRepository productRepository;
    private final StoreServices storeServices;
    private final StoreRepository storeRepository;


    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {
        Store store = storeRepository.findById(productDto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = ProductMapper.toEntity(productDto,store);
        Product saveProduct = productRepository.save(product);

        return ProductMapper.toDto(saveProduct);


//        return null;

    }


    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Store store = storeRepository.findById(productDto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));

        ProductMapper.updateEntity(product, productDto, store);

//        productRepository.save(product);
        Product saveProduct = productRepository.save(product);

        return ProductMapper.toDto(saveProduct);
    }

    @Override
    public void deleteProduct(Long id, User user) {

        //check it user is admin

        //then delete it
        // mark not available or not out of stock
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAllProductsByStoreId(Long storeId) {
        List<Product> products =productRepository.findByStoreId(storeId);

        return products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        List<Product> products =productRepository.searchByKeyword(storeId,keyword);

        return products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }
}
