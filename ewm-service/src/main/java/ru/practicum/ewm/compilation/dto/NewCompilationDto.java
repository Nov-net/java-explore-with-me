package ru.practicum.ewm.compilation.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.dto.EventShotDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {

    List<Long> events;

    boolean pinned; // Закреплена ли подборка на главной странице сайта example: true, default: false

    @NotBlank
    String title; // Заголовок подборки

}
