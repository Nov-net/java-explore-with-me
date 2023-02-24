package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto saveCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getCategory(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);

    boolean deleteCategory(Long catId);

}
