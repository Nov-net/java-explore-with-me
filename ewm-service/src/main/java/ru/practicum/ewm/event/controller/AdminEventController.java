package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Slf4j
public class AdminEventController {
    private final EventService service;

    /*PATCH /admin/events/{eventId} - обновление события*/
    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> adminUpdateEvent(@PathVariable Long eventId,
                                              @RequestBody @Valid UpdateEventRequest eventDto) {
        return new ResponseEntity<>(service.adminUpdateEvent(eventId, eventDto), HttpStatus.OK);
    }

    /*GET /admin/events - получение списка событий по параметрам*/
    @GetMapping()
    public ResponseEntity<Object> getEventsForAdmin(@RequestParam(required = false) List<Long> users,
                                                    @RequestParam(required = false) List<State> states,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(defaultValue = "2000-01-01 00:00:00", required = false) String rangeStart,
                                                    @RequestParam(defaultValue = "2100-01-01 00:00:00", required = false) String rangeEnd,
                                            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd,from, size),
                HttpStatus.OK);
    }

}
