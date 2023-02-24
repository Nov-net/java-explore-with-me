package ru.practicum.ewm.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.category.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category> {

    Category save(Category category);

    @Query("select u from Category u ")
    Page<Category> findAll(Pageable pageable);

    Optional<Category> findById(Long id);

    void delete(Category category);

}
