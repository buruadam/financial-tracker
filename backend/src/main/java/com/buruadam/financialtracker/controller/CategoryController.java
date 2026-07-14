package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.category.CategoryCreateRequest;
import com.buruadam.financialtracker.dto.category.CategoryResponseDto;
import com.buruadam.financialtracker.security.CustomUserDetails;
import com.buruadam.financialtracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryCreateRequest request, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        CategoryResponseDto response = categoryService.createCategory(request, currentUserDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getMyCategories(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        List<CategoryResponseDto> responses = categoryService.getMyCategories(currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }
}
