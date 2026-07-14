package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.CategoryRequest;
import com.buruadam.financialtracker.dto.CategoryResponse;
import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceAlreadyExistsException;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.repository.CategoryRepository;
import com.buruadam.financialtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponse createCategory(CategoryRequest request, UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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

    public List<CategoryResponse> getAllCategories(UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return categoryRepository.findByUser(currentUser).stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName(), category.getType()))
                .collect(Collectors.toList());
    }
}
