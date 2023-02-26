package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.SortEvent;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto saveEvent(Long userId, NewEventDto eventDto);

    EventFullDto userUpdateEvent(Long userId, Long eventId, UpdateEventRequest eventDto);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequest eventDto);

    List<EventShotDto> getEventsForUser(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    List<RequestDto> getRequests(Long userId, Long eventId);

    UpdateStatusRequestResult updateStatusRequest(Long userId, Long eventId, UpdateStatusRequest dto);

    List<EventFullDto> getEventsForAdmin(List<Long> users, List<State> states, List<Long> categories,
                                         String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventShotDto> getPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                                       Boolean onlyAvailable, SortEvent sortEvent, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getPublicEventById(Long eventId, HttpServletRequest request);

}
