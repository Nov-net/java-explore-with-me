package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentDtoShot;
import ru.practicum.ewm.comment.model.StateComment;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final CommentService service;

    /*PATCH /admin/comments/{commId} - обновление комментария*/
    @PatchMapping("/{commId}")
    public ResponseEntity<CommentDto> adminUpdateComment(@PathVariable  Long commId,
                                                    @RequestBody CommentDtoShot commentDto) {
        return new ResponseEntity<>(service.adminUpdateComment(commId, commentDto), HttpStatus.OK);
    }

    /*GET /admin/comments - получение списка комментов по параметрам*/
    @GetMapping()
    public ResponseEntity<List<CommentDto>> getCommentsForAdmin(@RequestParam(required = false) List<Long> comments,
                                     @RequestParam(required = false) List<Long> events,
                                     @RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<StateComment> states,
                                     @RequestParam(defaultValue = "2000-01-01 00:00:00", required = false) String rangeStart,
                                     @RequestParam(defaultValue = "2100-01-01 00:00:00", required = false) String rangeEnd,
                                     @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                     @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getCommentsForAdmin(comments, events, users, states, rangeStart, rangeEnd,from, size),
                HttpStatus.OK);
    }

}
