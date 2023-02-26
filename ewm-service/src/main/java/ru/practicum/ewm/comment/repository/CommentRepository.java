package ru.practicum.ewm.comment.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findAllByEventIdIsOrderById(Long eventId);

    void delete(Comment comment);

    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId);

    @Query("select u from Comment u where u.author.id = ?1")
    List<Comment> findAllByAuthorId(Long authorId, Pageable pageable);

    List<Comment> findByEventIdAndAuthorIdOrderById(Long eventId, Long authorId);

    List<Comment> findByEventId(Long eventId);

    Page<Comment> findAll(Predicate predicate, Pageable pageable);

}
