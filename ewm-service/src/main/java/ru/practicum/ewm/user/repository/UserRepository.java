package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    @Query("select u from User u ")
    Page<User> findAll(Pageable pageable);

    @Query("select u from User u where u.id in (?1) ")
    Page<User> findAllById(List<Long> ids, Pageable pageable);

    Optional<User> findById(Long id);

    void delete(User user);

}
