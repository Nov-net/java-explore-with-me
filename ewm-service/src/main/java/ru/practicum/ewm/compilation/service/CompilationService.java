package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto compilationDto);

    boolean deleteCompilation(Long compId);

    List<CompilationDto> getCompilation(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);

}
