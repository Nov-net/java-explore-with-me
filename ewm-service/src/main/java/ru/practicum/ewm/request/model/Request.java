package ru.practicum.ewm.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // Идентификатор заявки example: 3

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created; // Дата и время создания заявки example: 2022-09-06T21:10:05.432

    @Column(nullable = false)
    Long event; // Идентификатор события example: 1

    @Column(nullable = false)
    Long requester; // Идентификатор пользователя, отправившего заявку example: 2

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status status; // Статус заявки example: PENDING

}
