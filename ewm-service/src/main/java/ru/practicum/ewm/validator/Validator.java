package ru.practicum.ewm.validator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.error.ApiError;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class Validator {

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
    public static Category isValidCategory (Long catId, Optional<Category> category) {
        if (!category.isPresent()) {
            log.info("Категория не найдена");
            throw new NotFoundException("NOT_FOUND", "The required object was not found.",
                    String.format("Category with id=%d was not found", catId));
        }
        return category.get();
    }

    @SneakyThrows
    public static void isValidEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            log.info("Событие не может быть ранее, чем через два часа от текущего времени");
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %d", eventDate));
        }
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



}
