package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.category.CategoryCreateRequest;
import com.buruadam.financialtracker.dto.category.CategoryResponseDto;
import com.buruadam.financialtracker.entity.Category;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceAlreadyExistsException;
import com.buruadam.financialtracker.exception.ResourceNotFoundException;
import com.buruadam.financialtracker.mapper.CategoryMapper;
import com.buruadam.financialtracker.repository.CategoryRepository;
import com.buruadam.financialtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequest request, UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean exists = categoryRepository.existsByUserIdAndNameAndType(userId, request.name(), request.type());
        if (exists) {
            throw new ResourceAlreadyExistsException("Category with this name and type already exists for this user");
        }

        Category category = categoryMapper.toEntity(request);
        category.setUser(currentUser);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponseDto(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getMyCategories(UUID userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);

        return categories.stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }
}
