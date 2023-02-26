package ru.practicum.ewm.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.comment.model.StateComment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;

    @NotBlank
    @Size(min = 5, max = 7000)
    String text;

    String authorName;
    StateComment state;
    LocalDateTime created;
    LocalDateTime published;
}
