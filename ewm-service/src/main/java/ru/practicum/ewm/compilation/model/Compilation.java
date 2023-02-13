package ru.practicum.ewm.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "сompilations")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Transient
    List<Long> events;

    @Column(nullable = false)
    boolean pinned; // Закреплена ли подборка на главной странице сайта example: true

    @Column(nullable = false)
    String title; // Заголовок подборки

}
