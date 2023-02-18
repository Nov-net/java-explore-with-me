package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShotDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.State;

import java.util.List;

public interface EventService {

    EventFullDto saveEvent(Long userId, NewEventDto eventDto);

    EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventRequest eventDto);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequest eventDto);

    List<EventShotDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    List<EventFullDto> getEventsForAdmin(List<Long> users, List<State> states, List<Long> categories,
                                         String rangeStart, String rangeEnd, Integer from, Integer size);

}
