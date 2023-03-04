package ru.practicum.ewm.comment.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentDtoShot;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.QComment;
import ru.practicum.ewm.comment.model.StateComment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.comment.dto.CommentMapper.*;
import static ru.practicum.ewm.validator.Validator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*POST /users/{userId}/comments/{eventId} - добавление комментария*/
    @Override
    public CommentDto saveComment(Long userId, Long eventId, CommentDto commentDto) {
        log.info("Запрос на добавление комментария от userId={} для eventId={}: {}", userId, eventId, commentDto);

        User user = isValidUser(userId, userRepository.findById(userId));
        log.info("Нашли и проверили пользователя: {}", user);

        Event event = isValidEvent(eventId, eventRepository.findById(eventId));
        log.info("Нашли и проверили event: {}", event);

        Comment comment = toComment(commentDto);
        comment.setEvent(event);
        comment.setAuthor(user);
        log.info("Установили event и user");

        repository.save(comment);
        log.info("Создали и сохранили новый коммент: {}", comment);
        return toCommentDto(comment);

    }

    /*PATCH /users/{userId}/comments/{commId} - обновление комментария*/
    @Override
    public CommentDto userUpdateComment(Long userId, Long commId, CommentDto commentDto) {
        log.info("Запрос на обновления комментария commId={} от userId={}: {}", commId, userId, commentDto);

        User user = isValidUser(userId, userRepository.findById(userId));
        log.info("Нашли и проверили пользователя: {}", user);

        Comment comment = isValidComment(commId, repository.findById(commId));
        log.info("Нашли и проверили comment: {}", comment);

        isAuthor(userId, comment);
        log.info("Проверили userId == comment.getAuthor().getId()");

        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
            log.info("Обновлен text: {}", comment.getText());
        }

        if (commentDto.getState() != null && StateComment.CANCEL_REVIEW.equals(commentDto.getState())) {
            isCommentNotCanceledAndDtoNotReject(comment, commentDto);
            comment.setState(StateComment.REJECTED);
            log.info("Обновлен статус: {}", comment.getState());
        } else {
            comment.setState(StateComment.SEND_TO_REVIEW);
            log.info("Обновлен статус: {}", comment.getState());
        }

        return toCommentDto(repository.save(comment));
    }

    /*PATCH /admin/comments/{commId} - обновление комментария*/
    @Override
    public CommentDto adminUpdateComment(Long commId, CommentDtoShot commentDto) {
        log.info("Запрос admin на обновление комментария commId={}: {}", commId, commentDto);

        Comment comment = isValidComment(commId, repository.findById(commId));
        log.info("Нашли и проверили comment: {}", comment);

        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
            log.info("Обновлен text: {}", comment.getText());
        }

        if (commentDto.getState() != null) {
            if (StateComment.REJECTED.equals(commentDto.getState())) {
                comment.setState(StateComment.REJECTED);
                log.info("Установлен статус {}", comment.getState());
            }

            if (StateComment.PUBLISHED.equals(commentDto.getState())) {
                comment.setState(StateComment.PUBLISHED);
                comment.setPublished(LocalDateTime.now());
                log.info("Установлен статус {} и published {}", comment.getState(), comment.getPublished());
            }
        }

        return toCommentDto(repository.save(comment));
    }

    /*DELETE /users/{userId}/comments/{commId} - удаление комментария по id*/
    @Override
    public boolean deleteComment(Long userId, Long commId) {
        log.info("Запрос на удаление комментария commId={} от userId={}", commId, userId);
        User user = isValidUser(userId, userRepository.findById(userId));
        log.info("Нашли и проверили пользователя: {}", user);

        Comment comment = isValidComment(commId, repository.findById(commId));
        log.info("Нашли и проверили comment: {}", comment);

        isAuthor(userId, comment);
        log.info("Проверили userId == comment.getAuthor().getId()");

        repository.delete(isValidComment(commId, repository.findById(commId)));
        log.info("commId={} удален", commId);
        return true;
    }

    /*GET /users/{userId}/comments/{commId} - получение комментария по id*/
    @Override
    public CommentDto getCommentById(Long userId, Long commId) {
        log.info("Получение commId={} от userId={}", commId, userId);
        return toCommentDto(isValidComment(commId, repository.findByIdAndAuthorId(commId, userId)));
    }

    /*GET /users/{userId}/comments - получение списка своих комментов по userId*/
    @Override
    public List<CommentDto> getCommentByUserId(Long userId, Integer from, Integer size) {
        log.info("Получение списка comments userId={}, from={}, size={}", userId, from, size);
        Pageable pageable = PageRequest.of(from / size, size);

        return mapToCommentDto(repository.findAllByAuthorId(userId, pageable));
    }

    /*GET /users/{userId}/comments/{eventId} - получение списка комментов по eventId*/
    @Override
    public List<CommentDto> getCommentByUserIdAndEventId(Long userId, Long eventId) {
        log.info("Получение comments для eventId={} от userId={}", eventId, userId);
        return mapToCommentDto(repository.findByEventIdAndAuthorIdOrderById(eventId, userId));
    }

    /*GET /users/{userId}/comments/{eventId} - получение списка все комментов по eventId, где user == инициатор*/
    @Override
    public List<CommentDto> getCommentByEventId(Long userId, Long eventId) {
        log.info("Получение comments для eventId={} от userId={}", eventId, userId);
        Event event = isValidEvent(eventId, eventRepository.findById(eventId));
        log.info("Нашли и проверили event: {}", event);

        isInitiator(userId, event);
        log.info("Проверили инициатора");

        return mapToCommentDto(repository.findByEventId(eventId));
    }

    /*GET /admin/comments - получение списка комментов по параметрам*/
    @Override
    public List<CommentDto> getCommentsForAdmin(List<Long> comments, List<Long> events, List<Long> users,
                                                  List<StateComment> states, String rangeStart, String rangeEnd,
                                                  Integer from, Integer size) {
        log.info("Получение списка comments админом");

        List<BooleanExpression> predicates = new ArrayList<>();

        if (comments != null && !comments.isEmpty()) {
            predicates.add(QComment.comment.id.in(comments));
        }

        if (events != null && !events.isEmpty()) {
            predicates.add(QComment.comment.event.id.in(events));
        }

        if (users != null && !users.isEmpty()) {
            predicates.add(QComment.comment.author.id.in(users));
        }

        if (states != null && !states.isEmpty()) {
            predicates.add(QComment.comment.state.in(states));
        }

        predicates.add(QComment.comment.created.after(LocalDateTime.parse(rangeStart, pattern))
                .and(QComment.comment.created.before(LocalDateTime.parse(rangeEnd, pattern))));

        Pageable pageable = PageRequest.of(from / size, size);

        Predicate predicate = predicates.stream().reduce(BooleanExpression::and).get();

        return mapToCommentDto(repository.findAll(predicate, pageable).toList());
    }

}