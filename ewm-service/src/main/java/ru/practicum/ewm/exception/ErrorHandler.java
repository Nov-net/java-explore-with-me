package ru.practicum.ewm.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.error.ApiError;

import javax.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleIncorrectCountException(final InvalidParameterException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleAlreadyExistException(final AlreadyExistException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),
                HttpStatus.CONFLICT);
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }*/

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundException handleNotFoundException(final NotFoundException e) {
        return e;
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
        return new ResponseEntity<>(Map.of("error",e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()),
                HttpStatus.CONFLICT);
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        return new ApiError(e.getStatus(), e.getReason(), e.getMessage());
    }*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ForbiddenException handleForbiddenException(final ForbiddenException e) {
        return e;
    }

}
