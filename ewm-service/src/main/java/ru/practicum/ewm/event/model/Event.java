package ru.practicum.ewm.event.model;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.dto.UserShotDto;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String annotation; // Краткое описание example: Эксклюзивность нашего шоу гарантирует привлечение...

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column (name = "confirmed_requests")
    @Positive
    Integer confirmedRequests; // Количество одобренных заявок на участие в данном событии example: 5

    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss") example: 2022-09-06 11:00:23

    @Column(nullable = false)
    String description; // Полное описание события example: Что получится, если соединить кукурузу и полёт? ...

    @Column(name = "event_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate; // Дата и время события (в формате "yyyy-MM-dd HH:mm:ss") example: 2024-12-31 15:10:05

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @Column(nullable = false)
    boolean paid; // Нужно ли оплачивать участие

    @Column(name = "participant_limit", nullable = false)
    @PositiveOrZero
    int participantLimit; // Ограничение на количество участников, 0 - отсутствие ограничения example: 10 default: 0

    @Column(name = "published")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss") example: 2024-12-31 15:10:05

    @Column(name = "request_moderation",nullable = false)
    boolean requestModeration; // Нужна ли пре-модерация заявок на участие example: true default: true

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state; // Список состояний жизненного цикла события example: PUBLISHED

    @Column(nullable = false)
    String title; // Заголовок example: Знаменитое шоу 'Летающая кукуруза'

    @Column
    @Positive
    Long views; // Количество просмотрев события example: 999

}
