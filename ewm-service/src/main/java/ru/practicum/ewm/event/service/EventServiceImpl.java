package ru.practicum.ewm.event.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentMapper;
import ru.practicum.ewm.comment.model.StateComment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.dto.EventMapper.*;
import static ru.practicum.ewm.event.dto.EventMapper.mapToEventShotDto;
import static ru.practicum.ewm.event.model.State.*;
import static ru.practicum.ewm.event.model.StateAction.*;
import static ru.practicum.ewm.request.dto.RequestMapper.mapToRequestDto;
import static ru.practicum.ewm.validator.Validator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final CommentRepository commentRepository;
    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*POST /users/{userId}/events - добавление нового события юзером*/
    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto eventDto) {
        log.info("Получен новый event {}", eventDto);

        Event event = toEvent(eventDto);
        isValidEventDate(event.getEventDate());
        log.info("Время события проверено {}", event.getEventDate().format(pattern));

        event.setInitiator(isValidUser(userId, userRepository.findById(userId)));
        log.info("Событию установлен пользователь {}", event.getInitiator());

        event.setCategory(isValidCategory(eventDto.getCategory(), categoryRepository.findById(eventDto.getCategory())));
        log.info("Событию установлена категория {}", event.getCategory());

        Location location = locationRepository.findByLatAndLon(eventDto.getLocation().getLat(), eventDto.getLocation().getLon());

        if (location != null) {
            event.setLocation(location);
        } else {
            location = locationRepository.save(eventDto.getLocation());
            event.setLocation(location);
        }
        log.info("location события {}", location);

        Event newEvent = repository.save(event);
        log.info("Сохранен новый event {}", newEvent);

        return toEventFullDto(newEvent);
    }

    /*PATCH /users/{userId}/events/{eventId} - обновление события юзером*/
    @SneakyThrows
    @Override
    public EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventRequest eventDto) {
        log.info("Запрос на обновление event с id {} от пользователя с id {}: {}", eventId, userId, eventDto);

        if (eventDto.getStateAction() == null) {
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "To change an event, it must have one of the stateAction: SEND_TO_REVIEW or CANCEL_REVIEW");
        }

        Event event = isValidEvent(eventId, repository.findById(eventId));
        log.info("Получен {}", event);
        isInitiator(userId, event);
        log.info("Проверен инициатор");

        event = checkAndSetState(event, eventDto);

        if (eventDto.getEventDate() != null) {
            event.setEventDate(isValidEventDate(LocalDateTime.parse(eventDto.getEventDate(), pattern)));
            log.info("Время события проверено и установлено {}", event.getEventDate().format(pattern));
        }

        Event updEvent = repository.save(setData(event, eventDto));
        log.info("Event обновлен: {}", updEvent);
        return toEventFullDto(updEvent);
    }

    /*PATCH /admin/events/{eventId} - обновление события админом*/
    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequest eventDto) {
        log.info("Запрос admin на обновление event с id={}: {}", eventId, eventDto);

        Event event = isValidEvent(eventId, repository.findById(eventId));
        log.info("Получен {}", event);

        event = checkAndSetState(event, eventDto);

        if (eventDto.getEventDate() != null) {
            event.setEventDate(isValidAdminEventDate(LocalDateTime.parse(eventDto.getEventDate(), pattern)));
            log.info("Время события проверено и установлено {}", event.getEventDate().format(pattern));
        }

        Event updEvent = repository.save(setData(event, eventDto));
        log.info("Event обновлен: {}", updEvent);
        return toEventFullDto(updEvent);
    }

    /*GET /users/{userId}/events - получение списка событий добавленных текущим пользователем*/
    @Override
    public List<EventShotDto> getEventsForUser(Long userId, Integer from, Integer size) {
        log.info("Получение списка events пользователя userId={} from {}, size {}", userId, from, size);
        Pageable pageable = PageRequest.of(from / size, size);

        return mapToEventShotDto(repository.findAllByInitiatorId(userId, pageable)).stream()
                .map(this::setCommentsForEventShotDto)
                .collect(Collectors.toList());
    }

    /*GET /users/{userId}/events/{eventId} - получение события, добавленного текущим пользователем по id*/
    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        log.info("Получение event пользователя userId={}, eventId={}", userId, eventId);
        return setCommentsForEventFullDto(toEventFullDto(isValidEvent(eventId, repository.findByIdAndInitiatorId(eventId, userId))));
    }

    /*GET /users/{userId}/events/{eventId}/requests - получение запросов на участие в событии пользователя по eventId*/
    @Override
    public List<RequestDto> getRequests(Long userId, Long eventId) {
        log.info("Запрос на получение списка заявок на участие в eventId={}, инициатор userId={}", eventId, userId);
        Event event = isValidEvent(eventId, repository.findById(eventId));
        log.info("Проверен event");

        isInitiator(userId, event);
        log.info("Проверен инициатор");

        return mapToRequestDto(requestRepository.findAllByEvent(eventId));
    }

    /*PATCH /users/{userId}/events/{eventId}/requests - изменение статуса заявок на участие в событии пользователя по eventId*/
    @SneakyThrows
    @Override
    public UpdateStatusRequestResult updateStatusRequest(Long userId, Long eventId, UpdateStatusRequest dto) {
        log.info("Запрос на обновление статусов заявок eventId={} от userId={}: {}", eventId, userId, dto);

        Event event = isValidEvent(eventId, repository.findById(eventId));
        isInitiator(userId, event);
        log.info("Проверен инициатор");

        List<Request> list = requestRepository.findAllById(dto.getRequestIds());
        checkPendingStatusForListRequest(list);
        log.info("Проверен статус PENDING у заявок в количестве {}", list.size());

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        if (Status.CONFIRMED.equals(dto.getStatus())) {
            checkParticipantLimit(event);
            int available = event.getParticipantLimit() - event.getConfirmedRequests();
            log.info("Доступно к добавлению {} участников, запрос на добавление {} участников", available, dto.getRequestIds().size());

            if (available >= dto.getRequestIds().size()) {
                confirmedRequests = setRequestStatusConfirmed(list, confirmedRequests, event);

            } else {
                List<Request> listForConfirmed = new ArrayList<>();
                for (int i = 0; i < available; i++) {
                    listForConfirmed.add(list.remove(i));
                }
                confirmedRequests = setRequestStatusConfirmed(listForConfirmed, confirmedRequests, event);
                rejectedRequests = setRequestStatusRejected(list, rejectedRequests);
            }
        }

        if (Status.REJECTED.equals(dto.getStatus())) {
            rejectedRequests = setRequestStatusRejected(list, rejectedRequests);
        }

        return toUpdateStatusRequestResult(confirmedRequests, rejectedRequests);
    }

    /*GET /admin/events - получение админом списка событий по параметрам*/
    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<State> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("Получение списка events админом {}, {}, {}, {}, {}, {}, {}", users, states, categories, rangeStart, rangeEnd, from, size);

        List<BooleanExpression> predicates = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            predicates.add(QEvent.event.initiator.id.in(users));
        }

        if (states != null && !states.isEmpty()) {
            predicates.add(QEvent.event.state.in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(QEvent.event.category.id.in(categories));
        }

        predicates.add(QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, pattern))
                .and(QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, pattern))));

        Pageable pageable = PageRequest.of(from / size, size);

        Predicate predicate = predicates.stream()
                .reduce(BooleanExpression::and)
                .get();

        return mapToEventFullDto(repository.findAll(predicate, pageable).toList()).stream()
                .map(this::setCommentsForEventFullDto)
                .collect(Collectors.toList());
    }

    /*GET /events - получение списка событий по параметрам*/
    @Override
    public List<EventShotDto> getPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                              String rangeEnd, Boolean onlyAvailable, SortEvent sortEvent,
                                              Integer from, Integer size, HttpServletRequest request) {
        log.info("Получение list public event, параметры запроса: {}, {}, {}, {}, {}, {}, {}, {}, {}", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sortEvent, from, size);

        statsClient.saveHit(new HitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
                                                                     LocalDateTime.now().format(pattern)));
        log.info("Отправили статистику в statsClient: ewm-service, {}, {}", request.getRequestURI(), request.getRemoteAddr());

        List<BooleanExpression> predicates = new ArrayList<>();
        predicates.add(QEvent.event.state.eq(PUBLISHED));

        if (text != null && !text.isBlank()) {
            predicates.add(QEvent.event.annotation.likeIgnoreCase("%" + text + "%")
                    .or(QEvent.event.description.likeIgnoreCase("%" + text + "%")));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(QEvent.event.category.id.in(categories));
        }

        if (paid != null) {
            predicates.add(QEvent.event.paid.eq(paid));
        }

        if (rangeStart == null) {
            predicates.add(QEvent.event.eventDate.after(LocalDateTime.now())
                    .and(QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, pattern))));
        } else {
            predicates.add(QEvent.event.eventDate.after(LocalDateTime.parse(rangeStart, pattern))
                    .and(QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, pattern))));
        }

        if (onlyAvailable != null && Boolean.TRUE.equals(onlyAvailable)) {
            predicates.add(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests)));
        }

        Sort sort =  Sort.by("eventDate");

        if (SortEvent.VIEWS.equals(sortEvent)) {
            sort = Sort.by("views");
        }

        Pageable pageable = PageRequest.of(from / size, size, sort);

        Predicate predicate = predicates.stream().reduce(BooleanExpression::and).get();

        List<Event> list = repository.findAll(predicate, pageable).toList()
                .stream()
                .map(this::setViews)
                .collect(Collectors.toList());
        log.info("Установили новое значение просмотров списку events длиной {} элементов", list.size());

        return mapToEventShotDto(list).stream()
                .map(this::setCommentsForEventShotDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest request) {
        log.info("Получение public eventId={}", eventId);

        statsClient.saveHit(new HitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
                                                                    LocalDateTime.now().format(pattern)));
        log.info("Отправили статистику в statsClient: ewm-service, {}, {}", request.getRequestURI(), request.getRemoteAddr());

        return setCommentsForEventFullDto(toEventFullDto(setViews(isValidEvent(eventId, repository.findByIdAndState(eventId, PUBLISHED)))));
    }

    private Event setData(Event event, UpdateEventRequest eventDto) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
            log.info("setAnnotation {}", event.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            event.setCategory(isValidCategory(eventDto.getCategory(), categoryRepository.findById(eventDto.getCategory())));
            log.info("setCategory {}", event.getCategory());
        }

        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
            log.info("setDescription {}", event.getDescription());
        }

        if (eventDto.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(eventDto.getLocation().getLat(), eventDto.getLocation().getLon());

            if (location != null) {
                event.setLocation(location);
            } else {
                location = locationRepository.save(eventDto.getLocation());
                event.setLocation(location);
            }
            log.info("location события {}", location);
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
            log.info("setPaid {}", event.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
            log.info("setParticipantLimit {}", event.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
            log.info("setPaid {}", event.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
            log.info("setTitle {}", event.getTitle());
        }
        return event;
    }

    private Event checkAndSetState(Event event, UpdateEventRequest eventDto) {
        if (eventDto.getStateAction() != null) {
            isEventNotPublished(event);
            isEventNotCanceledAndDtoNotReject(event, eventDto);
            isEventNotCanceledAndDtoNotPublish(event, eventDto);

            if (eventDto.getStateAction().equals(CANCEL_REVIEW)) {
                event.setState(CANCELED);
                log.info("Установлен статус {}", event.getState());
                return event;
            }

            if (eventDto.getStateAction().equals(SEND_TO_REVIEW)) {
                event.setState(PENDING);
                log.info("Установлен статус {}", event.getState());
                return event;
            }

            if (eventDto.getStateAction().equals(PUBLISH_EVENT)) {
                event.setState(PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                log.info("Установлен статус {}, дата публикации {}", event.getState(), event.getPublishedOn().format(pattern));
                return event;
            }

            if (eventDto.getStateAction().equals(REJECT_EVENT)) {
                event.setState(CANCELED);
                log.info("Установлен статус {}", event.getState());
                return event;
            }
        }
        return event;
    }

    private Event setViews(Event event) {
        event.setViews(event.getViews() + 1);
        return repository.save(event);
    }

    private List<Request> setRequestStatusConfirmed(List<Request> list, List<Request> confirmedRequests, Event event) {
        for (Request r : list) {
            r.setStatus(Status.CONFIRMED);
            log.info("Заявке {} установлен статус CONFIRMED", r);

            confirmedRequests.add(requestRepository.save(r));
            log.info("Заявка сохранена и добавлена в list confirmedRequests");

            setEventConfirmedRequests(event);
        }

        return confirmedRequests;
    }

    private List<Request> setRequestStatusRejected(List<Request> listRequests, List<Request> rejectedRequests) {
        for (Request r : listRequests) {
            r.setStatus(Status.REJECTED);
            log.info("Заявке {} установлен статус REJECTED", r);
            rejectedRequests.add(requestRepository.save(r));
            log.info("Заявка сохранена и добавлена в list rejectedRequests");
        }
        return rejectedRequests;
    }

    private void setEventConfirmedRequests(Event event) {
        event.setConfirmedRequests((event.getConfirmedRequests() + 1));
        log.info("Событию установлен confirmedRequests {}", event.getConfirmedRequests());
        repository.save(event);
        log.info("Событие сохранено");
    }

    private EventFullDto setCommentsForEventFullDto(EventFullDto event) {
        event.setComments(getListCommentDto(event.getId()));
        log.info("Установили comments: {}", event.getComments());
        return event;
    }

    private EventShotDto setCommentsForEventShotDto(EventShotDto event) {
        event.setComments(getListCommentDto(event.getId()));
        log.info("Установили comments: {}", event.getComments());
        return event;
    }

    private List<CommentDto> getListCommentDto(Long eventId) {
        return commentRepository.findAllByEventIdAndStateIsOrderById(eventId, StateComment.PUBLISHED).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

}