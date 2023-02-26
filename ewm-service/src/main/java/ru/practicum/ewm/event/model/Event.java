package ru.practicum.ewm.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
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
    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column (name = "confirmed_requests")
    Integer confirmedRequests;

    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    @Column(nullable = false)
    String description;

    @Column(name = "event_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @Column(nullable = false)
    Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    Integer participantLimit;

    @Column(name = "published")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation",nullable = false)
    Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state;

    @Column(nullable = false)
    String title;

    @Column
    Long views;

}
