package ru.practicum.ewm.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToMany
    @JoinTable(name = "compilation_elements",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    List<Event> events;

    @Column
    Boolean pinned;

    @Column
    String title;

}
