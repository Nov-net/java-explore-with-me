package ru.practicum.ewm.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.dto.EventShotDto;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    List<EventShotDto> events;
    Long id;
    Boolean pinned;
    String title;

}
