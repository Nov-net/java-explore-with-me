package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShotDto;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShotDto {
    String annotation;
    CategoryDto category;
    Integer confirmedRequests;
    String eventDate;
    Long id;
    UserShotDto initiator;
    Location location;
    Boolean paid;
    String title;
    Long views;
    List<CommentDto> comments;
}
