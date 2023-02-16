package ru.practicum.ewm.exception.error;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError extends Exception{

    List<String> errors;

    String message; // 3

    String reason; // 2

    String status; // 1 enum?

    LocalDateTime timestamp; // 4

    public ApiError(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
