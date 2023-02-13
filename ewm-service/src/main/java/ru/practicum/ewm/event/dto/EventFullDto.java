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
public class EventFullDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation; // Краткое описание example: Эксклюзивность нашего шоу гарантирует привлечение...

    @NotNull
    CategoryDto category;

    Integer confirmedRequests; // Количество одобренных заявок на участие в данном событии example: 5
    String createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss") example: 2022-09-06 11:00:23

    @Size(min = 20, max = 7000)
    String description; // Полное описание события example: Что получится, если соединить кукурузу и полёт? ...

    @NotBlank
    String eventDate; // Дата и время события (в формате "yyyy-MM-dd HH:mm:ss") example: 2024-12-31 15:10:05

    Long id;

    @NotEmpty
    UserShotDto initiator;

    @NotBlank
    Location location;

    @NotEmpty
    boolean paid; // Нужно ли оплачивать участие

    @Positive
    int participantLimit; // Ограничение на количество участников, 0 - отсутствие ограничения example: 10 default: 0

    @NotBlank
    String publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss") example: 2024-12-31 15:10:05

    boolean requestModeration; // Нужна ли пре-модерация заявок на участие example: true default: true

    String state; // Список состояний жизненного цикла события example: PUBLISHED

    @NotBlank
    @Size(min = 3, max = 120)
    String title; // Заголовок example: Знаменитое шоу 'Летающая кукуруза'

    @Positive
    Long views; // Количество просмотрев события example: 999

}
