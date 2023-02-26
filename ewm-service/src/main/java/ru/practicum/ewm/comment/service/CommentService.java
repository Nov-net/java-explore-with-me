package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentDtoShot;
import ru.practicum.ewm.comment.model.StateComment;

import java.util.List;

public interface CommentService {

    CommentDto saveComment(Long userId, Long eventId, CommentDto commentDto);

    CommentDto userUpdateComment(Long userId, Long commId, CommentDto commentDto);

    CommentDto adminUpdateComment(Long commId, CommentDtoShot commentDto);

    boolean deleteComment(Long userId, Long commId);

    CommentDto getCommentById(Long userId, Long commId);

    List<CommentDto> getCommentByUserId(Long userId, Integer from, Integer size);

    List<CommentDto> getCommentByUserIdAndEventId(Long userId, Long eventId);

    List<CommentDto> getCommentByEventId(Long userId, Long eventId);

    List<CommentDto> getCommentsForAdmin(List<Long> comments, List<Long> events, List<Long> users,
                                         List<StateComment> states, String rangeStart, String rangeEnd,
                                         Integer from, Integer size);

}
