package ru.practicum.ewm.event.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventService {

    EventFullDto saveEvent(Long userId, NewEventDto eventDto);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto);
/*


    List<EventFullDto> getEvent(Integer from, Integer size);

    EventFullDto getEventById(Long eventId);

    boolean deleteEvent(Long eventId);*/

}
