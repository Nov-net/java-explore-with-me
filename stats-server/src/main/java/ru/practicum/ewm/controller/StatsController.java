package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@RequestBody @Valid HitDto hitDto) {
        return new ResponseEntity<>(service.saveHit(hitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start, @RequestParam String end,
                              @RequestParam (required = false) List<String> uris,
                              @RequestParam(defaultValue = "false", required = false) Boolean unique) {
        return new ResponseEntity<>(service.getStats(start, end, uris, unique), HttpStatus.OK);
    }

}
