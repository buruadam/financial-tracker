package com.buruadam.financialtracker.mapper;

import com.buruadam.financialtracker.dto.category.CategoryCreateRequest;
import com.buruadam.financialtracker.dto.category.CategoryResponseDto;
import com.buruadam.financialtracker.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto toResponseDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Category toEntity(CategoryCreateRequest request);
}
