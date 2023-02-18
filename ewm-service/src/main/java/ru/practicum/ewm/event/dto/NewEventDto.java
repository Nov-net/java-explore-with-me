package ru.practicum.ewm.event.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.StateAction;

import javax.validation.constraints.*;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation; //Краткое описание события maxLength: 2000 minLength: 20 example: Сплав на байдарках похож на полет.

    @NotNull
    Long category; // id категории к которой относится событие example: 2

    @NotBlank
    @Size(min = 20, max = 7000)
    String description; // Полное описание события maxLength: 7000  minLength: 20

    @NotBlank
    String eventDate; // Дата и время события в формате "yyyy-MM-dd HH:mm:ss" example: 2024-12-31 15:10:05

    Location location;

    boolean paid; // Нужно ли оплачивать участие в событии example: true default: false

    @PositiveOrZero
    int participantLimit; // Ограничение на количество участников, 0 - отсутствие ограничения example: 10 default: 0

    boolean requestModeration; // Нужна ли пре-модерация заявок на участие example: false default: true
    // true - все заявки ожидают подтверждения инициатором, false - подтверждаются автоматически

    @NotBlank
    @Size(min = 3, max = 120)
    String title; // Заголовок события maxLength: 120 minLength: 3 example: Сплав на байдарках

}
