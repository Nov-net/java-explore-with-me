package ru.practicum.ewm.event.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm.user.dto.UserMapper.toUserShotDto;

public class EventMapper {

    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory() != null ? toCategoryDto(event.getCategory()) : null)
                //.confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().toString())
                .description(event.getDescription())
                .eventDate(event.getEventDate().toString())
                .id(event.getId())
                .initiator(event.getInitiator() != null ? toUserShotDto(event.getInitiator()) : null)
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                //.publishedOn(event.getPublishedOn() != null ?  event.getPublishedOn().toString() : null)
                .requestModeration(event.isRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                //.views(event.getViews())
                .build();
    }

    public static List<EventFullDto> mapToEventDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static Event toEvent(NewEventDto event) {
        return Event.builder()
                .annotation(event.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(event.getDescription())
                .eventDate(LocalDateTime.parse(event.getEventDate(), pattern))
                //.location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .build();
    }

    public static Event toEvent(UpdateEventUserRequest event) {
        return Event.builder()
                .annotation(event.getAnnotation() != null ? event.getAnnotation() : null)
                .createdOn(LocalDateTime.now())
                .description(event.getDescription())
                .eventDate(LocalDateTime.parse(event.getEventDate(), pattern))
                //.location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .title(event.getTitle())
                .build();
    }

}