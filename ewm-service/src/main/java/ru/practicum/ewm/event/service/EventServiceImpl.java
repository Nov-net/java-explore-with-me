package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.validator.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static ru.practicum.ewm.category.dto.CategoryMapper.*;
import static ru.practicum.ewm.event.dto.EventMapper.toEvent;
import static ru.practicum.ewm.event.dto.EventMapper.toEventFullDto;
import static ru.practicum.ewm.event.model.State.PENDING;
import static ru.practicum.ewm.event.model.StateAction.CANCEL_REVIEW;
import static ru.practicum.ewm.event.model.StateAction.SEND_TO_REVIEW;
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

    /*POST /users/{userId}/events - добавление нового события*/
    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto eventDto) {
        log.info("Получен новый event {}", eventDto);
        //log.info("Получение пользователя по id {}", userId);
        /*Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            log.info("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }*/
        Event event = toEvent(eventDto);
        isValidEventDate(event.getEventDate());
        log.info("Время события проверено {}", event.getEventDate().toString());

        event.setInitiator(isValidUser(userId, userRepository.findById(userId)));
        log.info("Событию установлен пользователь {}", event.getInitiator());

        event.setCategory(isValidCategory(eventDto.getCategory(), categoryRepository.findById(eventDto.getCategory())));
        log.info("Событию установлена категория {}", event.getCategory());

        /*Optional<Category> category = categoryRepository.findById(eventDto.getCategory());
        if (!category.isPresent()) {
            log.info("Категория не найдена");
            throw new NotFoundException("Категория не найдена");
        }
        log.info("Категория события {}", category.get());
        event.setCategory(category.get());*/

        Location location = locationRepository.save(eventDto.getLocation());
        log.info("location события {}", location);
        event.setLocation(location);
        event.setState(PENDING); // вынести в маппер Event из NewEventDto, если NewEventDto приходит только как новый эвент с одним статусом

        //log.info("Итого поля event {}", event); // удалить перед отправкой на ревью

        Event newEvent = repository.save(event);
        log.info("Сохранен новый event {}", newEvent);

        return toEventFullDto(newEvent);
    }

    /*PATCH /users/{userId}/events/{eventId} - обновление события*/
    @SneakyThrows
    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        log.info("Запрос на обновление event с id {} от пользователя с id {}: {}", eventId, userId, eventDto);

        Event event = isValidEvent(eventId, repository.findById(eventId));
        log.info("Получен event по id {}", event);

        isInitiator(userId, event);

        if (eventDto.getStateAction().equals(SEND_TO_REVIEW) || eventDto.getStateAction().equals(CANCEL_REVIEW)) {
            throw new ForbiddenException("FORBIDDEN", "For the requested operation the conditions are not met.",
                    "For the requested operation the conditions are not met.");
        }

        isValidEventDate(event.getEventDate());
        log.info("Время события проверено {}", event.getEventDate().toString());

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

        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), pattern));
            log.info("setDescription {}", event.getEventDate());
        }

        if (eventDto.getLocation() != null) {
            Location location = locationRepository.save(eventDto.getLocation());
            event.setLocation(location);
            log.info("setLocation {}", event.getLocation());
        }

        if (eventDto.isPaid()) {
            event.setPaid(eventDto.isPaid());
            log.info("setPaid {}", event.isPaid());
        }

        if (!eventDto.isPaid()) {
            event.setPaid(eventDto.isPaid());
            log.info("setPaid {}", event.isPaid());
        }

        if (eventDto.getParticipantLimit() >= 0) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
            log.info("setParticipantLimit {}", event.getParticipantLimit());
        }

        if (eventDto.isRequestModeration()) {
            event.setPaid(eventDto.isRequestModeration());
            log.info("setPaid {}", event.isRequestModeration());
        }

        if (!eventDto.isRequestModeration()) {
            event.setPaid(eventDto.isRequestModeration());
            log.info("setPaid {}", event.isRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
            log.info("setTitle {}", event.getTitle());
        }

        Event updEvent = repository.save(event);
        log.info("Event обновлен: {}", updEvent);
        return toEventFullDto(updEvent);
    }

    /*


    *//*GET /admin/categories - получение списка категорий*//*
    @Override
    public List<CategoryDto> getCategory(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return mapToCategoryDto(repository.findAll(pageable).toList());
    }

    *//*GET /admin/categories/{catId} - получение категории по id*//*
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение категории по id {}", catId);
        Optional<Category> categoryOptional = repository.findById(catId);

        if (!categoryOptional.isPresent()) {
            log.info("Категория не найдена");
            throw new NotFoundException("Категория не найдена");
        }

        return toCategoryDto(categoryOptional.get());
    }

    *//*DELETE /users/{userId}/events/{eventId} - удаление события*//*
    @Override
    public boolean deleteCategory(Long catId) {
        log.info("Запрос на удаление категории с id {}", catId);

        log.info("Получение категории по id {}", catId);
        Optional<Category> categoryOptional = repository.findById(catId);

        if (!categoryOptional.isPresent()) {
            log.info("Категория не найдена");
            throw new NotFoundException("Категория не найдена");
        }

        // дописать поиск событий с данной категорией, если события есть, удалять нельзя 409 конфликт

        repository.delete(categoryOptional.get());
        log.info("Пользователь с id {} удален", catId);
        return true;
    }
*/



}