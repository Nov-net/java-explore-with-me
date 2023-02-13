package ru.practicum.ewm.event.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShotDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShotDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation; // Краткое описание example: Эксклюзивность нашего шоу гарантирует привлечение...

    @NotNull
    CategoryDto category;

    @Positive
    Integer confirmedRequests; // Количество одобренных заявок на участие в данном событии example: 5

    @NotBlank
    String eventDate; // Дата и время события (в формате "yyyy-MM-dd HH:mm:ss") example: 2024-12-31 15:10:05

    Long id;

    @NotEmpty
    UserShotDto initiator;

    @NotBlank
    Location location;

    @NotEmpty
    boolean paid; // Нужно ли оплачивать участие

    @NotBlank
    @Size(min = 3, max = 120)
    String title; // Заголовок example: Знаменитое шоу 'Летающая кукуруза'

    @Positive
    Long views; // Количество просмотрев события example: 999

}
