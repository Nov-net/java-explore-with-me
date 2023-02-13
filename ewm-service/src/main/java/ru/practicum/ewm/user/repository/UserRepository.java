package ru.practicum.ewm.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findById(Long id);

    void delete(User user);

    /*List<User> findAll();



    */

}
