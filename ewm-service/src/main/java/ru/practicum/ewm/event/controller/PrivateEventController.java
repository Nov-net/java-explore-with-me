package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class PrivateEventController {
    private final EventService service;

    /*POST /users/{userId}/events - добавление нового события*/
    @PostMapping
    public ResponseEntity<EventFullDto> saveEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto eventDto) {
        return new ResponseEntity<>(service.saveEvent(userId, eventDto), HttpStatus.CREATED);
    }

    /*PATCH /users/{userId}/events/{eventId} - обновление события*/
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                              @RequestBody @Valid UpdateEventRequest eventDto) {
        return new ResponseEntity<>(service.userUpdateEvent(userId, eventId, eventDto), HttpStatus.OK);
    }

    /*GET /users/{userId}/events - получение списка событий добавленных текущим пользователем*/
    @GetMapping()
    public ResponseEntity<List<EventShotDto>> getEvents(@PathVariable Long userId,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                        @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getEventsForUser(userId, from, size), HttpStatus.OK);
    }

    /*GET /users/{userId}/events/{eventId} - получение события, добавленного текущим пользователем по id*/
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getEventById(userId, eventId), HttpStatus.OK);
    }

    /*GET /users/{userId}/events/{eventId}/requests - получение запросов на участие в событии пользователя по eventId*/
    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getRequests(userId, eventId), HttpStatus.OK);
    }

    /*PATCH /users/{userId}/events/{eventId}/requests - изменение статуса заявок на участие в событии пользователя по eventId*/
    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<UpdateStatusRequestResult> updateStatusRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                      @RequestBody @Valid UpdateStatusRequest dto) {
        return new ResponseEntity<>(service.updateStatusRequest(userId, eventId, dto), HttpStatus.OK);
    }

}
