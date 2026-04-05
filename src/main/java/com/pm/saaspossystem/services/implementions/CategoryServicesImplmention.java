package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.CategoryMapper;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.Category;
import com.pm.saaspossystem.model.Store;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.CategoryDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.CategoryRepository;
import com.pm.saaspossystem.repository.StoreRepository;
import com.pm.saaspossystem.services.CategoryServices;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServicesImplmention implements CategoryServices {


    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) throws UserExceptions, IllegalAccessException {
        UserDto user =userService.getCurrentUser();


        log.info(STR."user details\{user.getId()},\{user.getStoreId()}");


       Store store = storeRepository.findById(user.getStoreId()).orElseThrow(() -> new UserExceptions("Store not Found"));
//       log.info(STR."store details\{store.getId()},\{store.getStoreAdmin().getId()}");


        Category category = Category.builder()
                .name(categoryDto.getName())
                .store(store)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info(STR."category details\{category.getStore()}");

// TODO: check it later
//        checkAuthortize(UserMapper.toEntity(user), store);


        // have to save
        Category saveCategory =categoryRepository.save(category);

        return CategoryMapper.toDto(saveCategory);
    }

    @Override
    public List<CategoryDto> getCategoriesByStore(Long storeId) {
//        return List.of();
        List<Category> categoryList =categoryRepository.findByStoreId(storeId);

        return categoryList.stream().map(CategoryMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws UserExceptions, IllegalAccessException {
        Category category = categoryRepository.findById(id).orElseThrow(()->new UserExceptions("categories not exist for this store"));
        UserDto user =userService.getCurrentUser();
//        // 2️⃣ Find category
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() ->
//                        new UserExceptions("Category does not exist"));
//A user from Store A could update Store B’s category ❌

        ///TODO: update it later
//        checkAuthortize(UserMapper.toEntity(user), category.getStore());

//        // 3️⃣ Check store ownership (VERY IMPORTANT)
//        if (!category.getStore().getId()
//                .equals(currentUser.getStore().getId())) {
//
//            throw new UserExceptions("You are not allowed to update this category");
//        }

        // 4️⃣ Update fields
        category.setName(categoryDto.getName());
//        category.setStore(categoryRepository.findById(categoryDto.getStoreId()));

        // 5️⃣ Save
        Category updatedCategory = categoryRepository.save(category);

        // 6️⃣ Return DTO
        return CategoryMapper.toDto(updatedCategory);

    }

    @Override
    public void deleteCategory(Long id) throws UserExceptions, IllegalAccessException {
        Category category = categoryRepository.findById(id).orElseThrow(()->new UserExceptions("categories not exist for this store"));
        UserDto user =userService.getCurrentUser();
        checkAuthortize(UserMapper.toEntity(user), category.getStore());
        categoryRepository.delete(category);

    }


    private void checkAuthortize(User user, Store store) throws IllegalAccessException {

//        Boolean isAdmin = user.getRole().equals(UserRole.ROLE_ADMIN);
//        Boolean isManager = user.getRole().equals(UserRole.ROLE_STORE_MANAGER);
//        Boolean isSameStoreManager = user.equals(store.getStoreAdmin());
//
//        log.info(STR."isAdmin\{isAdmin}, isManager\{isManager}, isSameStoreManager\{isSameStoreManager}");
//
//        if(!(isAdmin && isSameStoreManager) && !isManager){
//            throw  new IllegalAccessException("Illegel Access");
//        }

    }
}
