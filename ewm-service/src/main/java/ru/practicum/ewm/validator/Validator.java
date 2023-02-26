package ru.practicum.ewm.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.StateComment;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.ewm.event.model.State.CANCELED;
import static ru.practicum.ewm.event.model.State.PUBLISHED;
import static ru.practicum.ewm.event.model.StateAction.PUBLISH_EVENT;
import static ru.practicum.ewm.event.model.StateAction.REJECT_EVENT;

@Slf4j
public class Validator {
    private Validator() {
        throw new IllegalStateException("Utility class");
    }

    @SneakyThrows
    public static User isValidUser(Long userId, Optional<User> user) {
        if (!user.isPresent()) {
            log.info("Пользователь не найден");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        }
        return user.get();
    }

    @SneakyThrows
    public static Category isValidCategory(Long catId, Optional<Category> category) {
        if (!category.isPresent()) {
            log.info("Категория не найдена");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("Category with id=%d was not found", catId));
        }
        return category.get();
    }

    @SneakyThrows
    public static LocalDateTime isValidEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            log.info("Событие не может быть ранее, чем через два часа от текущего времени");
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", eventDate));
        }
        return eventDate;
    }

    @SneakyThrows
    public static LocalDateTime isValidAdminEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            log.info("Событие не может быть ранее, чем через час от текущего времени");
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", eventDate));
        }
        return eventDate;
    }

    @SneakyThrows
    public static Event isValidEvent(Long eventId, Optional<Event> event) {
        if (!event.isPresent()) {
            log.info("Event не найден");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("Event with id=%d was not found", eventId));
        }
        return event.get();
    }

    @SneakyThrows
    public static void isInitiator(Long userId, Event event) {
        if (!userId.equals(event.getInitiator().getId())) {
            log.info("userId != event.getInitiator().getId()");
            throw new ForbiddenException("BAD_REQUEST", "Incorrectly made request.",
                    String.format("Пользователь с userId %d не является инициатором события", userId));
        }
    }

    @SneakyThrows
    public static Request isValidRequest(Long requestId, Optional<Request> request) {
        if (!request.isPresent()) {
            log.info("Запрос не найден");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("Request with id=%d was not found", requestId));
        }
        return request.get();
    }

    @SneakyThrows
    public static boolean checkParticipantLimit(Event event) {
        if (event.getConfirmedRequests().equals(event.getParticipantLimit()) && !event.getParticipantLimit().equals(0)) {
            log.info("Достигнут предел количества участников: {} из {}", event.getConfirmedRequests(), event.getParticipantLimit());
            throw new ForbiddenException("CONFLICT", "For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }
        return true;
    }

    @SneakyThrows
    public static Compilation isValidCompilation(Long compId, Optional<Compilation> compilation) {
        if (compilation.isEmpty()) {
            log.info("Подборка не найдена");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("Compilation with id=%d was not found", compId));
        }
        return compilation.get();
    }

    @SneakyThrows
    public static void checkPendingStatusForListRequest(List<Request> list){
        for (Request r : list) {
            if (!r.getStatus().equals(Status.PENDING)) {
                throw new ForbiddenException("BAD_REQUEST", "Incorrectly made request.",
                        "Request must have status PENDING");
            }
        }
    }

    @SneakyThrows
    public static void isEventNotPublished(Event event) {
        if (event.getState().equals(PUBLISHED)) {
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "Cannot publish the event because it's not in the right state: PUBLISHED");
        }
    }

    @SneakyThrows
    public static void isEventNotCanceledAndDtoNotReject(Event event, UpdateEventRequest eventDto) {
        if (event.getState().equals(CANCELED) && eventDto.getStateAction().equals(REJECT_EVENT)) {
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "The event cannot be canceled because it has the status: CANCELED.");
        }
    }

    @SneakyThrows
    public static void isEventNotCanceledAndDtoNotPublish(Event event, UpdateEventRequest eventDto) {
        if (event.getState().equals(CANCELED) && eventDto.getStateAction().equals(PUBLISH_EVENT)) {
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "The event cannot be published because it has the status: CANCELED.");
        }
    }

    @SneakyThrows
    public static Comment isValidComment(Long commId, Optional<Comment> comment) {
        if (!comment.isPresent()) {
            log.info("Comment не найден");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("Comment with id=%d was not found", commId));
        }
        return comment.get();
    }

    @SneakyThrows
    public static void isCommentNotCanceledAndDtoNotReject(Comment comment, CommentDto commentDto) {
        if (StateComment.REJECTED.equals(comment.getState()) && StateComment.CANCEL_REVIEW.equals(commentDto.getState())) {
            throw new ForbiddenException("FORBIDDEN", "For the comment operation the conditions are not met.",
                    "The comment cannot be canceled because it has the status: REJECT.");
        }
    }

}
