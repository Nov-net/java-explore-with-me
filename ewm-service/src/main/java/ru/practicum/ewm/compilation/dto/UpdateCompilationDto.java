package ru.practicum.ewm.compilation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {

    List<Long> events;
    Boolean pinned;
    String title;

}
