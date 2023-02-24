package ru.practicum.ewm.event.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.*;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    @NotNull
    Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    String description;

    @NotBlank
    String eventDate;

    Location location;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;

    @NotBlank
    @Size(min = 3, max = 120)
    String title;

}
