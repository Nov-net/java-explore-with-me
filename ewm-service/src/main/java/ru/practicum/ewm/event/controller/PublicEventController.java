package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.SortEvent;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Slf4j
public class PublicEventController {
    private final EventService service;
    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*GET /events - получение списка событий по параметрам*/
    @GetMapping()
    public ResponseEntity<List<EventShotDto>> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(defaultValue = "2100-01-01 00:00:00", required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE", required = false) SortEvent sortEvent,
                                            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam(defaultValue = "10", required = false) Integer size,
                                            HttpServletRequest request) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().format(pattern);
        }
        return new ResponseEntity<>(service.getPublicEvents(text, categories, paid, rangeStart, rangeEnd,
                                                                onlyAvailable, sortEvent, from, size, request), HttpStatus.OK);
    }

    /*GET /events/{eventId} - получение события по id*/
    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getPublicEventById(@PathVariable Long id, HttpServletRequest request) {
        return new ResponseEntity<>(service.getPublicEventById(id, request), HttpStatus.OK);
    }

}
