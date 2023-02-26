package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Slf4j
public class AdminCategoryController {
    private final CategoryService service;

    /*POST /admin/categories - создание новой категории*/
    @PostMapping
    public ResponseEntity<Object> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(service.saveCategory(categoryDto), HttpStatus.CREATED);
    }

    /*PATCH /admin/categories/{catId} - обновление категории*/
    @PatchMapping("/{catId}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity<>(service.updateCategory(catId, categoryDto), HttpStatus.OK);
    }

    /*DELETE /admin/categories/{catId} - удаление категории по catId*/
    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        return new ResponseEntity<>(service.deleteCategory(catId), HttpStatus.NO_CONTENT);
    }

}
