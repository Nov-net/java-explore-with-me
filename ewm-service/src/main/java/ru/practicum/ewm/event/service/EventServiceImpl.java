package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShotDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewm.event.dto.EventMapper.*;
import static ru.practicum.ewm.event.model.State.*;
import static ru.practicum.ewm.event.model.StateAction.*;
import static ru.practicum.ewm.validator.Validator.*;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
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

        event.setState(PENDING); // вынести в маппер Event из NewEventDto, если NewEventDto приходит только как новый эвент с одним статусом

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
        log.info("Получен event по id {}", event);
        isInitiator(userId, event);

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
        log.info("Получен event по id {}", event);

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
    public List<EventShotDto> getEvents(Long userId, Integer from, Integer size) {
        log.info("Получение списка events пользователя userId={} from {}, size {}: {}", userId, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return mapToEventShotDto(repository.findAllByInitiatorId(userId, pageable));
    }

    /*GET /users/{userId}/events/{eventId} - получение события, добавленного текущим пользователем по id*/
    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        log.info("Получение event пользователя userId={}, eventId={}: {}", userId, eventId);
        return toEventFullDto(isValidEvent(eventId, repository.findByIdAndInitiatorId(eventId, userId)));
    }

    /*GET /admin/events - получение админом списка событий по параметрам*/
    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<State> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("Получение списка events админом {}, {}, {}, {}, {}, {}, {}", users, states, categories, rangeStart, rangeEnd, from, size);
        LocalDateTime start = LocalDateTime.parse(rangeStart, pattern);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, pattern);
        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> list = null;

        if (users == null && states == null && categories == null) {
            list = repository.findAllByEventDate(start, end, pageable);
        }

        if (users != null && states == null && categories == null) {
            list = repository.findAllByInitiatorIdAndByEventDate(users, start, end, pageable);
        }

        if (users != null && states != null && categories == null) {
            list = repository.findAllByInitiatorIdAndStateAndByEventDate(users, states, start, end, pageable);
        }

        if (users != null && states == null && categories != null) {
            list = repository.findAllByInitiatorIdAndCategoryIdAndEventDate(users, categories, start, end, pageable);
        }

        if (users != null && states != null && categories != null) {
            list = repository.findAllByInitiatorIdAndStateAndCategoryIdAndEventDate(users, states, categories, start, end, pageable);
        }

        if (users == null && states != null && categories == null) {
            list = repository.findAllByStateAndByEventDate(states, start, end, pageable);
        }

        if (users == null && states != null && categories != null) {
            list = repository.findAllByStateAndCategoryIdAndEventDate(states, categories, start, end, pageable);
        }

        if (users == null && states == null && categories != null) {
            list = repository.findAllByCategoryIdAndEventDate(categories, start, end, pageable);
        }


        return mapToEventFullDto(list);
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

        if (Boolean.TRUE.equals(eventDto.isPaid())) {
            event.setPaid(Boolean.TRUE);
            log.info("setPaid {}", event.isPaid());
        } else if (Boolean.FALSE.equals(eventDto.isPaid())) {
            event.setPaid(Boolean.FALSE);
            log.info("setPaid {}", event.isPaid());
        }

        if (eventDto.getParticipantLimit() >= 0) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
            log.info("setParticipantLimit {}", event.getParticipantLimit());
        }

        if (Boolean.FALSE.equals(eventDto.isRequestModeration())) {
            event.setRequestModeration(Boolean.FALSE);
            log.info("setPaid {}", event.isRequestModeration());
        } else if (Boolean.TRUE.equals(eventDto.isRequestModeration())) {
            event.setRequestModeration(Boolean.TRUE);
            log.info("setPaid {}", event.isRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
            log.info("setTitle {}", event.getTitle());
        }
        return event;
    }

    @SneakyThrows
    private Event checkAndSetState(Event event, UpdateEventRequest eventDto) {
        if (eventDto.getStateAction() != null) {

            if (event.getState().equals(PUBLISHED)) {
                throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "Cannot publish the event because it's not in the right state: PUBLISHED");
            }

            if (event.getState().equals(CANCELED) && eventDto.getStateAction().equals(REJECT_EVENT)) {
                throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                        "The event cannot be canceled because it has the status: CANCELED.");
            }

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



}