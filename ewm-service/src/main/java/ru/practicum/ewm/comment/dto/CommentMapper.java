package ru.practicum.ewm.comment.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.model.StateComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .state(comment.getState())
                .created(comment.getCreated())
                .published(comment.getPublished())
                .build();
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .state(StateComment.SEND_TO_REVIEW)
                .created(LocalDateTime.now())
                .build();
    }

}
