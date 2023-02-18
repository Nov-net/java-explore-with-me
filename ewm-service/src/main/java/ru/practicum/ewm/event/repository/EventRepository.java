package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event save(Event event);

    @Query("select u from Event u ")
    Page<Event> findAll(Pageable pageable);

    @Query("select u from Event u where u.initiator.id = ?1")
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findById(Long id);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    List<Event> findAllByCategoryId(Long catId);

    void delete(Event event);

    @Query("select u from Event u " +
            "where u.initiator.id in (?1) " +
            "and u.state in (?2) " +
            "and u.category.id in (?3) " +
            "and u.eventDate between ?4 and ?5 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByInitiatorIdAndStateAndCategoryIdAndEventDate(List<Long> users, List<State> states,
                                                                      List<Long> categories, LocalDateTime rangeStart,
                                                                      LocalDateTime rangeEnd, Pageable pageable);

    @Query("select u from Event u " +
            "where u.eventDate between ?1 and ?2 " +
            "order by u.id ")
    List<Event> findAllByEventDate(LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select u from Event u " +
            "where u.initiator.id in (?1) " +
            "and u.eventDate between ?2 and ?3 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByInitiatorIdAndByEventDate(List<Long> users, LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd, Pageable pageable);

    @Query("select u from Event u " +
            "where u.initiator.id in (?1) " +
            "and u.state in (?2) " +
            "and u.eventDate between ?3 and ?4 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByInitiatorIdAndStateAndByEventDate(List<Long> users, List<State> states, LocalDateTime rangeStart,
                                                           LocalDateTime rangeEnd, Pageable pageable);

    @Query("select u from Event u " +
            "where u.initiator.id in (?1) " +
            "and u.category.id in (?2) " +
            "and u.eventDate between ?3 and ?4 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByInitiatorIdAndCategoryIdAndEventDate(List<Long> users, List<Long> categories,
                                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                              Pageable pageable);

    @Query("select u from Event u " +
            "where u.state in (?1) " +
            "and u.eventDate between ?2 and ?3 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByStateAndByEventDate(List<State> states, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Pageable pageable);

    @Query("select u from Event u " +
            "where u.state in (?1) " +
            "and u.category.id in (?2) " +
            "and u.eventDate between ?3 and ?4 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByStateAndCategoryIdAndEventDate(List<State> states, List<Long> categories,
                                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select u from Event u " +
            "where u.category.id in (?1) " +
            "and u.eventDate between ?2 and ?3 " +
            "group by u.initiator.id, u.state, u.category.id, u.id " +
            "order by u.initiator.id ")
    List<Event> findAllByCategoryIdAndEventDate(List<Long> categories, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Pageable pageable);

}
