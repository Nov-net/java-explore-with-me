package ru.practicum.ewm.exception.error;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiError extends Exception {
    String message;
    String reason;
    String status;
    String timestamp;

    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ApiError(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(pattern);
    }
}
