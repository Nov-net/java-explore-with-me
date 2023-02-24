package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Slf4j
public class PublicCompilationController {
    private final CompilationService service;

    /*GET /compilations - получение списка категорий*/
    @GetMapping()
    public ResponseEntity<List<CompilationDto>> getCompilation(@RequestParam(required = false) Boolean pinned,
                                                               @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                               @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getCompilation(pinned, from, size), HttpStatus.OK);
    }

    /*GET /compilations/{compId} - получение категории по id*/
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long compId) {
        return new ResponseEntity<>(service.getCompilationById(compId), HttpStatus.OK);
    }

}
