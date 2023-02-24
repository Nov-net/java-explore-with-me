package ru.practicum.ewm.event.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Event save(Event event);

    void delete(Event event);

    @Query("select u from Event u where u.initiator.id = ?1")
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findById(Long eventId);

    Optional<Event> findByIdAndState(Long eventId,State state);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    List<Event> findAllByCategoryId(Long catId);

    List<Event> findAllByIdIn(List<Long> eventsId);

    Page<Event> findAll(Predicate predicate, Pageable pageable);

}
