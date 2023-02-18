package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class PrivateEventController {
    private final EventService service;

    /*POST /users/{userId}/events - добавление нового события*/
    @PostMapping
    public ResponseEntity<Object> saveEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto eventDto) {
        return new ResponseEntity<>(service.saveEvent(userId, eventDto), HttpStatus.CREATED);
    }

    /*PATCH /users/{userId}/events/{eventId} - обновление события*/
    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                              @RequestBody @Valid UpdateEventRequest eventDto) {
        return new ResponseEntity<>(service.userUpdateEvent(userId, eventId, eventDto), HttpStatus.OK);
    }

    /*GET /users/{userId}/events - получение списка событий добавленных текущим пользователем*/
    @GetMapping()
    public ResponseEntity<Object> getEvents(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getEvents(userId, from, size), HttpStatus.OK);
    }

    /*GET /users/{userId}/events/{eventId} - получение события, добавленного текущим пользователем по id*/
    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getEventById(userId, eventId), HttpStatus.OK);
    }

    /*
    *//*DELETE /users/{userId}/events/{eventId} - удаление события*//*
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long catId) {
        return new ResponseEntity<>(service.deleteEvent(catId), HttpStatus.NO_CONTENT);
    }

*/
}
