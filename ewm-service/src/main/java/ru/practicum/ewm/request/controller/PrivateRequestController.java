package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.*;
import ru.practicum.ewm.request.service.RequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
public class PrivateRequestController {
    private final RequestService service;

    /*POST /users/{userId}/requests?eventId={eventId} - создание новой заявки на участие в мероприятии*/
    @PostMapping
    public ResponseEntity<Object> saveRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return new ResponseEntity<>(service.saveRequest(userId, eventId), HttpStatus.CREATED);
    }

    /*PATCH /users/{userId}/requests/{requestId}/cancel - отмена своей заявки*/
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @PathVariable Long requestId) {
        return new ResponseEntity<>(service.updateRequest(userId, requestId), HttpStatus.OK);
    }

    /*GET /users/{userId}/requests - получение заявок текущего пользователя*/
    @GetMapping()
    public ResponseEntity<Object> getRequest(@PathVariable Long userId) {
        return new ResponseEntity<>(service.getRequest(userId), HttpStatus.OK);
    }

}
