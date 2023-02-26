package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class AdminCompilationController {
    private final CompilationService service;

    /*POST /admin/compilations - создание новой подборки*/
    @PostMapping
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        return new ResponseEntity<>(service.saveCompilation(compilationDto), HttpStatus.CREATED);
    }

    /*PATCH /admin/compilations/{compId} - обновление подборки*/
    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId,
                                                    @RequestBody @Valid UpdateCompilationDto compilationDto) {
        return new ResponseEntity<>(service.updateCompilation(compId, compilationDto), HttpStatus.OK);
    }

    /*DELETE /admin/compilations/{compId} - удаление подборки по compId*/
    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        return new ResponseEntity<>(service.deleteCompilation(compId), HttpStatus.NO_CONTENT);
    }

}
