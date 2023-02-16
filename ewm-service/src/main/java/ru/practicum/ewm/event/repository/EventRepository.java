package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event save(Event event);

    @Query("select u from Event u ")
    Page<Event> findAll(Pageable pageable);

    Optional<Event> findById(Long id);

    void delete(Event event);

}
