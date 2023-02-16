package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static ru.practicum.ewm.category.dto.CategoryMapper.*;
import static ru.practicum.ewm.validator.Validator.isValidCategory;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    /*POST /admin/categories - создание новой категории*/
    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        log.info("Получена новая категория {}", categoryDto);
        Category category = repository.save(toCategory(categoryDto));
        log.info("Сохранена новая категория {}", category);
        return toCategoryDto(category);
    }

    /*PATCH /admin/categories/{catId} - обновление категории*/
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Запрос на обновление категории с id {}: {}", catId, categoryDto);

        Category category = isValidCategory(catId, repository.findById(catId));
        category.setName(categoryDto.getName());

        Category updCategory = repository.save(category);
        log.info("Категория обновлена: {}", updCategory);
        return toCategoryDto(updCategory);
    }

    /*GET /admin/categories - получение списка категорий*/
    @Override
    public List<CategoryDto> getCategory(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return mapToCategoryDto(repository.findAll(pageable).toList());
    }

    /*GET /admin/categories/{catId} - получение категории по id*/
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение категории по id {}", catId);
        return toCategoryDto(isValidCategory(catId, repository.findById(catId)));
    }

    /*DELETE /admin/categories/{catId} - удаление категории по catId*/
    @Override
    public boolean deleteCategory(Long catId) {
        log.info("Запрос на удаление категории с id {}", catId);

        // дописать поиск событий с данной категорией, если события есть, удалять нельзя 409 конфликт

        repository.delete(isValidCategory(catId, repository.findById(catId)));
        log.info("Пользователь с id {} удален", catId);
        return true;
    }

}