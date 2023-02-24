package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.request.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm.request.dto.RequestMapper.mapToRequestDto;
import static ru.practicum.ewm.user.dto.UserMapper.toUserShotDto;

public class EventMapper {

    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto event) {
        return Event.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(event.getDescription())
                .eventDate(LocalDateTime.parse(event.getEventDate(), pattern))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration() != null ? event.getRequestModeration() : true)
                .state(State.PENDING)
                .title(event.getTitle())
                .views(0L)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory() != null ? toCategoryDto(event.getCategory()) : null)
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : null)
                .createdOn(event.getCreatedOn().format(pattern))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(pattern))
                .id(event.getId())
                .initiator(event.getInitiator() != null ? toUserShotDto(event.getInitiator()) : null)
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() != null ?  event.getPublishedOn().format(pattern) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews() != null ? event.getViews() : null)
                .build();
    }

    public static List<EventFullDto> mapToEventFullDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static EventShotDto toEventShotDto(Event event) {
        return EventShotDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory() != null ? toCategoryDto(event.getCategory()) : null)
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : null)
                .eventDate(event.getEventDate().format(pattern))
                .id(event.getId())
                .initiator(event.getInitiator() != null ? toUserShotDto(event.getInitiator()) : null)
                .location(event.getLocation())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews() != null ? event.getViews() : null)
                .build();
    }

    public static List<EventShotDto> mapToEventShotDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShotDto)
                .collect(Collectors.toList());
    }

    public static UpdateStatusRequestResult toUpdateStatusRequestResult (List<Request> confirmedRequests,
                                                                         List<Request> rejectedRequests) {
        return UpdateStatusRequestResult.builder()
                .confirmedRequests(mapToRequestDto(confirmedRequests))
                .rejectedRequests(mapToRequestDto(rejectedRequests))
                .build();
    }

}