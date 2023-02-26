package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Slf4j
public class PublicCategoryController {
    private final CategoryService service;

    /*GET /admin/categories - получение списка категорий*/
    @GetMapping()
    public ResponseEntity<Object> getCategory(@PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                          @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getCategory(from, size), HttpStatus.OK);
    }

    /*GET /admin/categories/{catId} - получение категории по id*/
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        return new ResponseEntity<>(service.getCategoryById(catId), HttpStatus.OK);
    }

}
