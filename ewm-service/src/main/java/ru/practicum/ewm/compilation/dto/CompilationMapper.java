package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.compilation.model.Compilation;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.dto.EventMapper.mapToEventShotDto;

public class CompilationMapper {

    static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(mapToEventShotDto(compilation.getEvents()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> mapToCompilationDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .pinned(compilationDto.getPinned() != null ? compilationDto.getPinned() : false)
                .title(compilationDto.getTitle())
                .build();
    }

}