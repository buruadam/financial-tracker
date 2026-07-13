package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.CategoryRequest;
import com.buruadam.financialtracker.dto.CategoryResponse;
import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceAlreadyExistsException;
import com.buruadam.financialtracker.repository.CategoryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        User currentUser = getCurrentUser();

        categoryRepository.findByUserIdAndNameAndType(currentUser.getId(), request.name(), request.type())
                .ifPresent(_ -> {
                    throw new ResourceAlreadyExistsException(
                            String.format("Category already exists with name '%s' and type '%s'", request.name(), request.type())
                    );

                });

        Category category = new Category();
        category.setName(request.name());
        category.setType(request.type());
        category.setUser(currentUser);

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory.getId(), savedCategory.getName(), savedCategory.getType());
    }

    public List<CategoryResponse> getAllCategories() {
        User currentUser = getCurrentUser();

        return categoryRepository.findByUser(currentUser).stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName(), category.getType()))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
