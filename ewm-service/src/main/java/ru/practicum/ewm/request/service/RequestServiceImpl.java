package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.ewm.event.model.State.PUBLISHED;
import static ru.practicum.ewm.request.dto.RequestMapper.*;
import static ru.practicum.ewm.request.model.Status.*;
import static ru.practicum.ewm.validator.Validator.*;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /*POST /users/{userId}/requests?eventId={eventId} - создание новой заявки на участие в мероприятии*/
    @SneakyThrows
    @Override
    public RequestDto saveRequest(Long userId, Long eventId) {
        log.info("Получена новая заявка от пользователя id={} на участие в мероприятии id={}", userId, eventId);

        Event event = isValidEvent(eventId, eventRepository.findById(eventId));
        log.info("Получен и проверен {}", event);
        checkParticipantLimit(event);
        int available = event.getParticipantLimit() - event.getConfirmedRequests();
        log.info("Event проверен, доступно к добавлению {} участников", available);

        if (!event.getState().equals(PUBLISHED)) {
            log.info("Событие не опубликовано, текущий статус {}", event.getState());
            throw new ForbiddenException("CONFLICT", "You can't participate in an unpublished event.",
                    String.format("Event with id=%d has %s status", eventId, event.getState()));
        }
        log.info("Проверен event");

        isValidUser(userId, userRepository.findById(userId));
        if (userId.equals(event.getInitiator().getId())) {
            log.info("Заявка от инициатора мероприятия");
            throw new ForbiddenException("CONFLICT", "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_request]; nested exception is " +
                            "org.hibernate.exception.ConstraintViolationException: could not execute statement");
        }
        log.info("Проверен user");

        if (repository.findAllByRequesterAndEvent(userId, eventId).isPresent()) {
            log.info("Заявка уже существует");
            throw new ForbiddenException("CONFLICT", "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_request]; nested exception is " +
                            "org.hibernate.exception.ConstraintViolationException: could not execute statement");
        }

        Request request = toRequest(userId, eventId);
        log.info("Заявка создана {}", request);

        if (Boolean.FALSE.equals(event.getRequestModeration())) {
            if (event.getParticipantLimit() == 0) {
                request.setStatus(CONFIRMED);
                log.info("Событие не требует модерации заявок, установлен статус {}", request.getStatus());
            } else {
                checkParticipantLimit(event);
                request.setStatus(CONFIRMED);
                log.info("Заявке {} установлен статус CONFIRMED", request);

                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                log.info("Событию установлен ConfirmedRequests {}", event.getConfirmedRequests());
                eventRepository.save(event);
                log.info("Событие сохранено");
            }
        }

        return toRequestDto(repository.save(request));
    }

    /*PATCH /users/{userId}/requests/{requestId}/cancel - отмена своей заявки*/
    @Override
    public RequestDto updateRequest(Long userId, Long requestId) {
        log.info("Запрос на отмену заявки id={} от userId={}", requestId, userId);
        Request request = isValidRequest(requestId, repository.findById(requestId));

        if (userId.equals(request.getRequester())) {
            request.setStatus(CANCELED);
        }

        request = repository.save(request);
        log.info("Заявка отменена {}", request);
        return toRequestDto(request);
    }

    /*GET /users/{userId}/requests - получение заявок текущего пользователя*/
    @Override
    public List<RequestDto> getRequest(Long userId) {
        log.info("Запрос на получение списка заявок userId={}", userId);
        return mapToRequestDto(repository.findAllByRequester(userId));
    }

}