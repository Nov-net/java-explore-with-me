package ru.practicum.ewm.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.comment.model.StateComment;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDtoShot {
    String text;
    StateComment state;
}
