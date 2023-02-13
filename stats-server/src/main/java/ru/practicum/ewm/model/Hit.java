package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String app;

    @Column
    private String uri;

    @Column
    private String ip;

    @Column
    private LocalDateTime timestamp;

}
