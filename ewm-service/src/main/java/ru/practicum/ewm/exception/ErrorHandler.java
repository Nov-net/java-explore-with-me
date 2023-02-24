package ru.practicum.ewm.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(Map.of("status", "BAD_REQUEST",
                "reason", "Incorrectly made request.",
                "message", String.format("Field: %s. Error: %s. Value: %s", e.getFieldError().getField(),
                        e.getFieldError().getDefaultMessage(), e.getFieldError().getRejectedValue()),
                "timestamp", LocalDateTime.now().format(pattern)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleAlreadyExistException(final AlreadyExistException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(Map.of("status", e.getStatus(), "reason", e.getReason(),
                "message", e.getMessage(), "timestamp", e.getTimestamp()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleInternalServerError(final EntityNotFoundException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleUnknownState(final UnknownState e) {
        return new ResponseEntity<>(Map.of("status", e.getStatus(), "reason", e.getReason(),
                "message", e.getMessage(), "timestamp", e.getTimestamp()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ResponseEntity<>(Map.of("status", "CONFLICT", "reason", "Integrity constraint has been violated.",
                "message", e.getMessage(), "timestamp", LocalDateTime.now().format(pattern)),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleForbiddenException(final ForbiddenException e) {
        return new ResponseEntity<>(Map.of("status", e.getStatus(), "reason", e.getReason(),
                "message", e.getMessage(), "timestamp", e.getTimestamp()), HttpStatus.CONFLICT);
    }

}
