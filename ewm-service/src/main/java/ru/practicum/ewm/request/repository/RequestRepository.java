package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request save(Request request);

    List<Request> findAllByRequester(Long requester);

    Optional<Request> findAllByRequesterAndEvent(Long requester, Long event);

    Optional<Request> findById(Long id);

    List<Request> findAllByEvent(Long event);

    @Query("select u from Request u " +
            "where u.id in (?1) ")
    List<Request> findAllById(List<Long> id);

    void delete(Request request);

}
