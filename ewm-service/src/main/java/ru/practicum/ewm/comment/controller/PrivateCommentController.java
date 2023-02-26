package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService service;

    /*POST /users/{userId}/comments/{eventId} - добавление комментария*/
    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> saveComment(@PathVariable  Long userId,
                                                  @PathVariable  Long eventId,
                                                  @RequestBody @Valid CommentDto commentDto) {
        return new ResponseEntity<>(service.saveComment(userId, eventId, commentDto), HttpStatus.CREATED);
    }

    /*PATCH /users/{userId}/comments/{commId} - обновление комментария*/
    @PatchMapping("/{commId}")
    public ResponseEntity<CommentDto> userUpdateComment(@PathVariable  Long userId,
                                                @PathVariable  Long commId,
                                                @RequestBody @Valid CommentDto commentDto) {
        return new ResponseEntity<>(service.userUpdateComment(userId, commId, commentDto), HttpStatus.OK);
    }

    /*DELETE /users/{userId}/comments/{commId} - удаление комментария по id*/
    @DeleteMapping("/{commId}")
    public ResponseEntity<Object> deleteComment(@PathVariable  Long userId, @PathVariable  Long commId) {
        return new ResponseEntity<>(service.deleteComment(userId, commId), HttpStatus.NO_CONTENT);
    }

    /*GET /users/{userId}/comments/{commId} - получение комментария по id*/
    @GetMapping("/{commId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable  Long userId, @PathVariable  Long commId) {
        return new ResponseEntity<>(service.getCommentById(userId, commId), HttpStatus.OK);
    }

    /*GET /users/{userId}/comments - получение списка своих комментов по userId*/
    @GetMapping()
    public ResponseEntity<List<CommentDto>> getCommentByUserId(@PathVariable  Long userId,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                     @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ResponseEntity<>(service.getCommentByUserId(userId, from, size), HttpStatus.OK);
    }

    /*GET /users/{userId}/comments/{eventId} - получение списка комментов по eventId*/
    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentByUserIdAndEventId(@PathVariable  Long userId, @PathVariable  Long eventId) {
        return new ResponseEntity<>(service.getCommentByUserIdAndEventId(userId, eventId), HttpStatus.OK);
    }

    /*GET /users/{userId}/comments/{eventId} - получение списка все комментов по eventId, где user == инициатор*/
    @GetMapping("/all/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentByEventId(@PathVariable  Long userId, @PathVariable  Long eventId) {
        return new ResponseEntity<>(service.getCommentByEventId(userId, eventId), HttpStatus.OK);
    }

}
