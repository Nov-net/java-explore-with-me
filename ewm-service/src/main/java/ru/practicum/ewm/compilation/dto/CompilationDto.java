package ru.practicum.ewm.compilation.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.dto.EventShotDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    List<EventShotDto> events;

    @NotNull
    Long id;

    @NotNull
    boolean pinned; // Закреплена ли подборка на главной странице сайта example: true

    @NotBlank
    String title; // Заголовок подборки

}
