package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.StateAction;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    String eventDate;

    Location location;

    boolean paid;

    @PositiveOrZero
    int participantLimit;

    boolean requestModeration;

    StateAction stateAction;

    @Size(min = 3, max = 120)
    String title;

}
