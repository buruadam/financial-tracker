package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.CategoryRequest;
import com.buruadam.financialtracker.dto.CategoryResponse;
import com.buruadam.financialtracker.security.CustomUserDetails;
import com.buruadam.financialtracker.service.CategoryService;
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
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request, @AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        CategoryResponse response = categoryService.createCategory(request, currentUserDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {
        List<CategoryResponse> responses = categoryService.getAllCategories(currentUserDetails.getId());
        return ResponseEntity.ok(responses);
    }
}
