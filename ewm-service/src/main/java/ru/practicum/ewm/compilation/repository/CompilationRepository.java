package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Compilation save(Compilation compilation);

    void delete(Compilation compilation);

    @Query("select u from Compilation u ")
    Page<Compilation> findAll(Pageable pageable);

    @Query("select u from Compilation u where u.pinned = ?1 ")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    Optional<Compilation> findById(Long id);

}
