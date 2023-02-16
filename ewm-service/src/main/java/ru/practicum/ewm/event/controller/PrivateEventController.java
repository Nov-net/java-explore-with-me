package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;

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
                                              @RequestBody @Valid UpdateEventUserRequest eventDto) {
        return new ResponseEntity<>(service.updateEvent(userId, eventId, eventDto), HttpStatus.OK);
    }

    /*
    *//*DELETE /users/{userId}/events/{eventId} - удаление события*//*
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long catId) {
        return new ResponseEntity<>(service.deleteEvent(catId), HttpStatus.NO_CONTENT);
    }

*/
}
